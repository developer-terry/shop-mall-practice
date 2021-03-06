package com.practice.shopmall.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.practice.common.exception.NoStockException;
import com.practice.common.to.mq.OrderTo;
import com.practice.common.to.mq.StockDetailTo;
import com.practice.common.to.mq.StockLockedTo;
import com.practice.common.utils.R;
import com.practice.shopmall.ware.entity.WareOrderTaskDetailEntity;
import com.practice.shopmall.ware.entity.WareOrderTaskEntity;
import com.practice.shopmall.ware.feign.OrderFeignService;
import com.practice.shopmall.ware.feign.ProductFeignService;
import com.practice.shopmall.ware.service.WareOrderTaskDetailService;
import com.practice.shopmall.ware.service.WareOrderTaskService;
import com.practice.shopmall.ware.vo.*;
import com.rabbitmq.client.Channel;
import lombok.Data;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.practice.common.utils.PageUtils;
import com.practice.common.utils.Query;

import com.practice.shopmall.ware.dao.WareSkuDao;
import com.practice.shopmall.ware.entity.WareSkuEntity;
import com.practice.shopmall.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;

@RabbitListener(queues = "stock.release.stock.queue")
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    WareOrderTaskService wareOrderTaskService;

    @Autowired
    WareOrderTaskDetailService wareOrderTaskDetailService;

    @Autowired
    OrderFeignService orderFeignService;

    /**
     * ??????????????????
     * ??????????????? ?????????????????? ??????????????????????????????????????????????????? ?????????????????????????????????????????????
     *
     * ????????????????????????????????????
     *
     * ????????????????????????????????? ??????????????????????????????
     */
//    @RabbitHandler
//    public void handleStockLockedRelease(StockLockedTo stockLockedTo, Message message, Channel channel) throws IOException {
//        System.out.println("???????????????????????????");
//        StockDetailTo detailTo = stockLockedTo.getDetail();
//        Long detailId = detailTo.getId();
//
//        //???????????????????????????????????????????????????
//        //??????????????? ??????????????????????????????
//        //?????? ????????????
//        //  ?????????????????? ????????????
//        //  ???????????????
//        //      ????????????????????????????????????
//        //      ??????????????????????????????
//
//        //?????????????????? ?????????????????? ??????????????? ????????????????????????
//        WareOrderTaskDetailEntity wareOrderTaskDetailEntity = wareOrderTaskDetailService.getById(detailId);
//        if(wareOrderTaskDetailEntity != null) {
//            Long id = stockLockedTo.getId();
//            WareOrderTaskEntity wareOrderTaskEntity = wareOrderTaskService.getById(id);
//            String orderSn = wareOrderTaskEntity.getOrderSn();
//            R r = orderFeignService.getOrderStatus(orderSn);
//            if(r.getCode() == 0) {
//                OrderVo orderVo = r.getData(new TypeReference<OrderVo>() {});
//
//                //??????????????????????????????????????????????????? ?????????????????????
//                //????????????????????? ??????????????????
//                if(orderVo == null || orderVo.getStatus() == 4) {
//                    unLockStock(
//                            detailTo.getSkuId(),
//                            detailTo.getWareId(),
//                            detailTo.getSkuNum(),
//                            detailId
//                    );
//
//                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//                }
//            } else {
//                channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
//            }
//        } else {
//            //  ????????????
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        }
//    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                new QueryWrapper<WareSkuEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {

        List<WareSkuEntity> wareSkuEntities = this.baseMapper.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if(wareSkuEntities == null || wareSkuEntities.size() == 0) {
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setStockLocked(0);
            //TODO ???????????? sku ??????, ???????????? ??????????????? rollback
            //TODO ?????????????????????????????? rollback ???
            try {
                R r = productFeignService.info(skuId);
                Map<String, Object> data = (Map<String, Object>) r.get("skuInfo");

                if(r.getCode() == 0) {
                    wareSkuEntity.setSkuName((String) data.get("skuName"));
                }
            } catch (Exception e) {

            }

            this.baseMapper.insert(wareSkuEntity);
        } else {
            this.baseMapper.addStock(skuId, wareId, skuNum);
        }
    }

    @Override
    public List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds) {
        List<SkuHasStockVo> SkuHasStockVos = skuIds.stream().map(skuId -> {
            SkuHasStockVo skuHasStockVo = new SkuHasStockVo();
            Long stock = baseMapper.getSkuStock(skuId);
            skuHasStockVo.setSkuId(skuId);
            skuHasStockVo.setHasStock(stock == null ? false : stock > 0);
            return skuHasStockVo;
        }).collect(Collectors.toList());

        return SkuHasStockVos;
    }

    /**
     * ????????????????????????
     * @param wareSkuLockVo
     * @return
     *
     * ??????????????????
     *  ??????????????? ????????????????????????????????????????????? ????????????????????? ??????????????????
     *
     *  ??????????????? ?????????????????? ??????????????????????????????????????????????????? ?????????????????????????????????????????????
     */
//    @Transactional(rollbackFor = NoStockException.class)
    @Transactional
    @Override
    public Boolean orderLockStock(WareSkuLockVo wareSkuLockVo) {
        /**
         * ??????????????????????????????
         * ??????
         */
        WareOrderTaskEntity wareOrderTaskEntity = new WareOrderTaskEntity();
        wareOrderTaskEntity.setOrderSn(wareSkuLockVo.getOrderSn());
        wareOrderTaskService.save(wareOrderTaskEntity);


        //??????????????????????????? ???????????????????????? ????????????(??????)

        //?????????????????????????????????????????????
        List<OrderItemVo> locks = wareSkuLockVo.getLocks();
        List<SkuWareHasStock> collect = locks.stream().map(lock -> {
            SkuWareHasStock stock = new SkuWareHasStock();
            Long skuId = lock.getSkuId();
            stock.setSkuId(skuId);
            stock.setNum(lock.getCount());
            List<Long> wareIds = baseMapper.listWareIdHasSkuStock(skuId);
            stock.setWareId(wareIds);
            return stock;
        }).collect(Collectors.toList());

        for (SkuWareHasStock hasStock : collect) {
            Boolean skuStockLocked = false;
            Long skuId = hasStock.getSkuId();
            List<Long> wareIds = hasStock.getWareId();
            if(wareIds == null || wareIds.size() == 0) {
                throw new NoStockException(skuId);
            }
            //???????????????????????????????????? ?????????????????????????????????????????????????????? MQ
            //???????????? ?????????????????????????????????????????? ?????????????????????????????????????????? ??????????????????????????? id ????????????????????????
            //
            for (Long wareId : wareIds) {
                //???????????? 1 ????????? 0
                Long count = baseMapper.lockSkuStock(skuId, wareId, hasStock.getNum());
                if(count == 1) {
                    skuStockLocked = true;
                    //TODO ?????? MQ ??????????????????
                    WareOrderTaskDetailEntity wareOrderTaskDetailEntity = new WareOrderTaskDetailEntity(
                            null,
                            skuId,
                            "",
                            hasStock.getNum(),
                            wareOrderTaskEntity.getId(),
                            wareId,
                            1
                    );

                    wareOrderTaskDetailService.save(wareOrderTaskDetailEntity);
                    StockLockedTo stockLockedTo = new StockLockedTo();
                    stockLockedTo.setId(wareOrderTaskEntity.getId());
                    StockDetailTo stockDetailTo = new StockDetailTo();
                    BeanUtils.copyProperties(wareOrderTaskDetailEntity, stockDetailTo);
                    //?????? id ?????? ?????????????????????????????????
                    stockLockedTo.setDetail(stockDetailTo);
                    rabbitTemplate.convertAndSend("stock-event-exchange", "stock.locked", stockLockedTo);
                    break;
                } else {
                    //????????????????????????
                }
            }

            if(!skuStockLocked) {
                throw new NoStockException(skuId);
            }
        }

        return true;
    }

    @Override
    public void unlockStock(StockLockedTo stockLockedTo) {
        StockDetailTo detailTo = stockLockedTo.getDetail();
        Long detailId = detailTo.getId();

        //???????????????????????????????????????????????????
        //??????????????? ??????????????????????????????
        //?????? ????????????
        //  ?????????????????? ????????????
        //  ???????????????
        //      ????????????????????????????????????
        //      ??????????????????????????????

        //?????????????????? ?????????????????? ??????????????? ????????????????????????
        WareOrderTaskDetailEntity wareOrderTaskDetailEntity = wareOrderTaskDetailService.getById(detailId);
        if(wareOrderTaskDetailEntity != null) {
            Long id = stockLockedTo.getId();
            WareOrderTaskEntity wareOrderTaskEntity = wareOrderTaskService.getById(id);
            String orderSn = wareOrderTaskEntity.getOrderSn();
            R r = orderFeignService.getOrderStatus(orderSn);
            if(r.getCode() == 0) {
                OrderVo orderVo = r.getData(new TypeReference<OrderVo>() {});

                //??????????????????????????????????????????????????? ?????????????????????
                //????????????????????? ??????????????????
                if(orderVo == null || orderVo.getStatus() == 4) {

                    if(wareOrderTaskDetailEntity.getLockStatus() == 1) {
                        unLockStock(
                                detailTo.getSkuId(),
                                detailTo.getWareId(),
                                detailTo.getSkuNum(),
                                detailId
                        );
                    }
//                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                }
            } else {
                throw new RuntimeException("??????????????????");
//                channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
            }
        } else {
            //  ????????????
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    //???????????????????????? ??????????????????????????????????????? ???????????????????????? ??????????????????????????? ????????????????????????
    //????????????????????? ???????????????????????????
    @Transactional
    @Override
    public void unlockStock(OrderTo orderTo) {
        String orderSn = orderTo.getOrderSn();
        //?????????????????????????????? ????????????????????????
        WareOrderTaskEntity wareOrderTaskEntity = wareOrderTaskService.getOrderTaskByOrderSn(orderSn);
        Long wareOrderTaskId = wareOrderTaskEntity.getId();
        //???????????????????????????????????????????????? ????????????
        List<WareOrderTaskDetailEntity> entities = wareOrderTaskDetailService.list(new QueryWrapper<WareOrderTaskDetailEntity>()
                .eq("task_id", wareOrderTaskId)
                .eq("lock_status", 1)
        );

        for (WareOrderTaskDetailEntity entity : entities) {
            unLockStock(entity.getSkuId(), entity.getWareId(), entity.getSkuNum(), entity.getId());
        }
    }

    private void unLockStock(Long skuId, Long wareId, Integer num, Long taskDetailId) {
        this.baseMapper.unLockStock(skuId, wareId, num);

        WareOrderTaskDetailEntity wareOrderTaskDetailEntity = new WareOrderTaskDetailEntity();
        wareOrderTaskDetailEntity.setTaskId(taskDetailId);
        wareOrderTaskDetailEntity.setLockStatus(2);
        wareOrderTaskDetailService.updateById(wareOrderTaskDetailEntity);
    }

    @Data
    class SkuWareHasStock{
        private Long skuId;
        private List<Long> wareId;
        private Integer num;
    }
}