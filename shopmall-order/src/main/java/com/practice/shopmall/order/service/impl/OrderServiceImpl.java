package com.practice.shopmall.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.practice.common.exception.NoStockException;
import com.practice.common.to.mq.OrderTo;
import com.practice.common.to.mq.SeckillOrderTo;
import com.practice.common.utils.R;
import com.practice.common.vo.MemberResponseVo;
import com.practice.shopmall.order.constant.OrderConstant;
import com.practice.shopmall.order.dao.OrderItemDao;
import com.practice.shopmall.order.entity.OrderItemEntity;
import com.practice.shopmall.order.enume.OrderStatusEnum;
import com.practice.shopmall.order.feign.CartFeignService;
import com.practice.shopmall.order.feign.MemberFeignService;
import com.practice.shopmall.order.feign.ProductFeignService;
import com.practice.shopmall.order.feign.WmsFeignService;
import com.practice.shopmall.order.interceptor.LoginUserInterceptor;
import com.practice.shopmall.order.service.OrderItemService;
import com.practice.shopmall.order.to.OrderCreateTo;
import com.practice.shopmall.order.vo.*;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.practice.common.utils.PageUtils;
import com.practice.common.utils.Query;

import com.practice.shopmall.order.dao.OrderDao;
import com.practice.shopmall.order.entity.OrderEntity;
import com.practice.shopmall.order.service.OrderService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    private ThreadLocal<OrderSubmitVo> orderSubmitVoThreadLocal = new ThreadLocal<>();

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    MemberFeignService memberFeignService;

    @Autowired
    CartFeignService cartFeignService;

    @Autowired
    WmsFeignService wmsFeignService;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();

        MemberResponseVo memberResponseVo = LoginUserInterceptor.loginUser.get();

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();

        CompletableFuture<Void> getAddressFuture = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<MemberRecieveAddressVo> address = memberFeignService.getAddress(memberResponseVo.getId());
            orderConfirmVo.setAddresses(address);
        }, threadPoolExecutor);

        CompletableFuture<Void> cartFuture = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<OrderItemVo> orderItemVoList = cartFeignService.getCurrentUserCartItems();
            orderConfirmVo.setItems(orderItemVoList);
        }, threadPoolExecutor).thenRunAsync(() -> {
            List<OrderItemVo> items = orderConfirmVo.getItems();
            List<Long> skuIds = items.stream().map(item -> item.getSkuId()).collect(Collectors.toList());
            R r = wmsFeignService.getSkusHasStock(skuIds);
            List<SkuHasStockVo> skuHasStockVoList = r.getData(new TypeReference<List<SkuHasStockVo>>() {
            });

            if (skuHasStockVoList != null) {
                Map<Long, Boolean> stockMap = skuHasStockVoList.stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, SkuHasStockVo::getHasStock));
                orderConfirmVo.setStocks(stockMap);
            }
        }, threadPoolExecutor);

        Integer integration = memberResponseVo.getIntegration();
        orderConfirmVo.setIntegration(integration);

        //TODO ????????????
        String token = UUID.randomUUID().toString().replace("-", "");
        stringRedisTemplate.opsForValue().set(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberResponseVo.getId(), token, 30, TimeUnit.MINUTES);
        orderConfirmVo.setOrderToken(token);

        CompletableFuture.allOf(getAddressFuture, cartFuture).get();

        return orderConfirmVo;
    }

    @GlobalTransactional
    @Transactional
    @Override
    public SubmitOrderResponseVo submitOrder(OrderSubmitVo orderSubmitVo) {

        orderSubmitVoThreadLocal.set(orderSubmitVo);

        SubmitOrderResponseVo submitOrderResponseVo = new SubmitOrderResponseVo();

        MemberResponseVo memberResponseVo = LoginUserInterceptor.loginUser.get();

        //?????????????????????????????????????????????
        //return 0 or 1
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        String orderToken = orderSubmitVo.getOrderToken();
        Long result = stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberResponseVo.getId()), orderToken);

        // ??????????????????
        if (result == 0L) {
            submitOrderResponseVo.setCode(1);
            return submitOrderResponseVo;
        }

        OrderCreateTo orderCreateTo = createOrder();
        //??????
        BigDecimal payAmount = orderCreateTo.getOrder().getPayAmount();
        BigDecimal payPrice = orderSubmitVo.getPayPrice();
        if(Math.abs(payAmount.subtract(payPrice).doubleValue()) < 0.01) {
            // ????????????
            // ??????????????????
            saveOrder(orderCreateTo);
            // ????????????
            WareSkuLockVo wareSkuLockVo = new WareSkuLockVo();
            wareSkuLockVo.setOrderSn(orderCreateTo.getOrder().getOrderSn());
            List<OrderItemVo> orderItemVoList = orderCreateTo.getOrderItems().stream().map(orderItemEntity -> {
                OrderItemVo orderItemVo = new OrderItemVo();
                orderItemVo.setSkuId(orderItemEntity.getSkuId());
                orderItemVo.setCount(orderItemEntity.getSkuQuantity());
                orderItemVo.setTitle(orderItemEntity.getSkuName());
                return orderItemVo;
            }).collect(Collectors.toList());
            wareSkuLockVo.setLocks(orderItemVoList);
            //??????????????????
            //????????????????????? ???????????????????????? ??????????????????????????????
            //??????????????????????????????????????????????????? ????????????
            R r = wmsFeignService.orderLockStock(wareSkuLockVo);
            if(r.getCode() == 0){
                //????????????
                submitOrderResponseVo.setOrder(orderCreateTo.getOrder());

                rabbitTemplate.convertAndSend("order-event-exchange", "order.create.order", orderCreateTo.getOrder());

                return submitOrderResponseVo;
            } else {
                //????????????
                throw new NoStockException(0L);
//                submitOrderResponseVo.setCode(3);
//                return submitOrderResponseVo;
            }
        } else {
            // ????????????
            submitOrderResponseVo.setCode(2);
            return submitOrderResponseVo;
        }

//        String token = stringRedisTemplate.opsForValue().get(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberResponseVo.getId());
//        if(orderToken != null && orderToken.equals(token)) {
//            stringRedisTemplate.delete(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberResponseVo.getId());
//        }
    }

    @Override
    public OrderEntity getOrderByOrderSn(String orderSn) {
        return this.getOne(new QueryWrapper<OrderEntity>().eq("order_sn", orderSn));
    }

    @Override
    public void closeOrder(OrderEntity entity) {
        OrderEntity orderEntity = this.getById(entity.getId());

        if(orderEntity != null && orderEntity.getStatus() == OrderStatusEnum.CREATE_NEW.getCode()) {

            OrderEntity update = new OrderEntity();
            update.setId(entity.getId());
            update.setStatus(OrderStatusEnum.CANCLED.getCode());
            this.updateById(update);

            OrderTo order = new OrderTo();
            BeanUtils.copyProperties(orderEntity, order);

            try {
                //TODO ?????????????????????????????? ??????????????????????????????????????????(????????????????????????????????????????????????)
                //TODO ????????????????????? ??????????????????????????????
                rabbitTemplate.convertAndSend("order-event-exchange", "order.release.other", order);
            } catch (Exception e) {
                //TODO ?????????????????????????????????????????????

            }
        }
    }

    @Override
    public void createSeckillOrder(SeckillOrderTo seckillOrderTo) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(seckillOrderTo.getOrderSn());
        orderEntity.setMemberId(seckillOrderTo.getMemberId());
        orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        BigDecimal payAmount = seckillOrderTo.getSeckillPrice().multiply(new BigDecimal("" + seckillOrderTo.getNum()));
        orderEntity.setPayAmount(payAmount);
        this.save(orderEntity);

        //????????????????????????
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setOrderSn(seckillOrderTo.getOrderSn());
        orderItemEntity.setRealAmount(payAmount);
        orderItemEntity.setSkuQuantity(seckillOrderTo.getNum());
        orderItemService.save(orderItemEntity);
    }

    private void saveOrder(OrderCreateTo orderCreateTo) {
        OrderEntity orderEntity = orderCreateTo.getOrder();
        orderEntity.setModifyTime(new Date());
        this.save(orderEntity);
        List<OrderItemEntity> orderItemEntities = orderCreateTo.getOrderItems();
        orderItemService.saveBatch(orderItemEntities);
    }

    private OrderCreateTo createOrder() {
        OrderCreateTo orderCreateTo = new OrderCreateTo();
        //?????????????????????
        String orderSn = IdWorker.getTimeId();
        OrderEntity orderEntity = buildOrder(orderSn);
        List<OrderItemEntity> orderItemEntities = buildOrderItems(orderSn);
        //??????
        computePrice(orderEntity, orderItemEntities);
        orderCreateTo.setOrder(orderEntity);
        return orderCreateTo;
    }

    private void computePrice(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntities) {
        // ??????????????????
        BigDecimal total = new BigDecimal("0");
        BigDecimal coupon = new BigDecimal("0");
        BigDecimal integration = new BigDecimal("0");
        BigDecimal promotion = new BigDecimal("0");
        Integer giftIntegration = 0;
        Integer giftGrowth = 0;

        for (OrderItemEntity orderItemEntity : orderItemEntities) {
            BigDecimal skuPrice = orderItemEntity.getRealAmount();
            BigDecimal couponAmount = orderItemEntity.getCouponAmount();
            BigDecimal integrationAmount = orderItemEntity.getIntegrationAmount();
            BigDecimal promotionAmount = orderItemEntity.getPromotionAmount();
            Integer giftIntegrationAmount = orderItemEntity.getGiftIntegration();
            Integer giftGrowthAmount = orderItemEntity.getGiftGrowth();

            total = total.add(skuPrice);
            coupon = coupon.add(couponAmount);
            integration = integration.add(integrationAmount);
            promotion = promotion.add(promotionAmount);
            giftIntegration += giftIntegrationAmount;
            giftGrowth += giftGrowthAmount;
        }

        //????????????
        orderEntity.setTotalAmount(total);
        orderEntity.setPayAmount(total.add(orderEntity.getFreightAmount()));
        orderEntity.setPromotionAmount(promotion);
        orderEntity.setIntegrationAmount(integration);
        orderEntity.setCouponAmount(coupon);

        //????????????
        orderEntity.setIntegration(giftIntegration);
        orderEntity.setGrowth(giftGrowth);
        orderEntity.setDeleteStatus(0);
    }

    private OrderEntity buildOrder(String orderSn) {
        MemberResponseVo memberResponseVo = LoginUserInterceptor.loginUser.get();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(orderSn);
        orderEntity.setMemberId(memberResponseVo.getId());

        //????????????????????????
        OrderSubmitVo orderSubmitVo = orderSubmitVoThreadLocal.get();
        R fare = wmsFeignService.getFare(orderSubmitVo.getAddrId());
        FareVo fareVo = fare.getData(new TypeReference<FareVo>() {
        });

        //??????????????????
        orderEntity.setFreightAmount(fareVo.getFare());
        //?????????????????????
        orderEntity.setReceiverCity(fareVo.getAddress().getCity());
        orderEntity.setReceiverDetailAddress(fareVo.getAddress().getDetailAddress());
        orderEntity.setReceiverName(fareVo.getAddress().getName());
        orderEntity.setReceiverPhone(fareVo.getAddress().getPhone());
        orderEntity.setReceiverPostCode(fareVo.getAddress().getPostCode());
        orderEntity.setReceiverProvince(fareVo.getAddress().getProvince());
        orderEntity.setReceiverRegion(fareVo.getAddress().getRegion());

        orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        orderEntity.setAutoConfirmDay(7);

        return orderEntity;
    }

    private List<OrderItemEntity> buildOrderItems(String orderSn) {
        //????????????????????????
        List<OrderItemVo> currentUserCartItems = cartFeignService.getCurrentUserCartItems();
        if (currentUserCartItems != null && currentUserCartItems.size() > 0) {
            List<OrderItemEntity> orderItemEntities = currentUserCartItems.stream().map(currentUserCartItem -> {
                OrderItemEntity orderItemEntity = buildOrderItem(currentUserCartItem);
                orderItemEntity.setOrderSn(orderSn);

                return orderItemEntity;
            }).collect(Collectors.toList());

            return orderItemEntities;
        }

        return null;
    }

    private OrderItemEntity buildOrderItem(OrderItemVo currentUserCartItem) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();

        //????????? spu
        Long skuId = currentUserCartItem.getSkuId();
        R spuInfo = productFeignService.getSpuInfoBySkuId(skuId);
        SpuInfoVo spuInfoVo = spuInfo.getData(new TypeReference<SpuInfoVo>() {
        });

        orderItemEntity.setSpuId(spuInfoVo.getId());
        orderItemEntity.setSpuBrand(spuInfoVo.getBrandId().toString());
        orderItemEntity.setSpuName(spuInfoVo.getSpuName());
        orderItemEntity.setCategoryId(spuInfoVo.getCatalogId());

        //????????? sku
        orderItemEntity.setSkuId(skuId);
        orderItemEntity.setSkuName(currentUserCartItem.getTitle());
        orderItemEntity.setSkuPic(currentUserCartItem.getImage());
        orderItemEntity.setSkuPrice(currentUserCartItem.getPrice());
        String skuAttr = StringUtils.collectionToDelimitedString(currentUserCartItem.getSkuAttr(), ";");
        orderItemEntity.setSkuAttrsVals(skuAttr);
        orderItemEntity.setSkuQuantity(currentUserCartItem.getCount());

        //????????????
        //????????????
        orderItemEntity.setGiftGrowth(currentUserCartItem.getPrice().multiply(new BigDecimal(currentUserCartItem.getCount().toString())).intValue());
        orderItemEntity.setGiftIntegration(currentUserCartItem.getPrice().multiply(new BigDecimal(currentUserCartItem.getCount().toString())).intValue());

        //????????????????????????
        orderItemEntity.setPromotionAmount(new BigDecimal("0"));
        orderItemEntity.setCouponAmount(new BigDecimal("0"));
        orderItemEntity.setIntegrationAmount(new BigDecimal("0"));
        //??????????????????
        BigDecimal originalPrice = orderItemEntity.getSkuPrice().multiply(new BigDecimal(orderItemEntity.getSkuQuantity().toString()));
        BigDecimal realAmount = originalPrice
                .subtract(orderItemEntity.getSkuPrice())
                .subtract(orderItemEntity.getCouponAmount())
                .subtract(orderItemEntity.getIntegrationAmount());
        orderItemEntity.setRealAmount(realAmount);

        return orderItemEntity;
    }
}