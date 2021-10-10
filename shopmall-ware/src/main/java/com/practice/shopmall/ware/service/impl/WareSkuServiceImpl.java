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
     * 庫存自動解鎖
     * 下訂單成功 庫存鎖定成功 但接下來的業務調用失敗導致訂單回滾 這時之前鎖定的庫存就要自動解鎖
     *
     * 訂單失敗起因於鎖庫存失敗
     *
     * 只要解鎖庫存的消息失敗 就要告訴服務解鎖失敗
     */
//    @RabbitHandler
//    public void handleStockLockedRelease(StockLockedTo stockLockedTo, Message message, Channel channel) throws IOException {
//        System.out.println("收到解鎖庫存的消息");
//        StockDetailTo detailTo = stockLockedTo.getDetail();
//        Long detailId = detailTo.getId();
//
//        //查詢資料庫關於這個訂單的鎖庫存訊息
//        //如果有找到 只能證明庫存鎖定成功
//        //解鎖 訂單情況
//        //  沒有這個訂單 必須解鎖
//        //  有這個訂單
//        //      訂單狀態已取消就解鎖庫存
//        //      沒取消就不能解鎖庫存
//
//        //如果沒有找到 庫存鎖定失敗 庫存回滾了 這種情況無需解鎖
//        WareOrderTaskDetailEntity wareOrderTaskDetailEntity = wareOrderTaskDetailService.getById(detailId);
//        if(wareOrderTaskDetailEntity != null) {
//            Long id = stockLockedTo.getId();
//            WareOrderTaskEntity wareOrderTaskEntity = wareOrderTaskService.getById(id);
//            String orderSn = wareOrderTaskEntity.getOrderSn();
//            R r = orderFeignService.getOrderStatus(orderSn);
//            if(r.getCode() == 0) {
//                OrderVo orderVo = r.getData(new TypeReference<OrderVo>() {});
//
//                //產生訂單流程因為發生錯誤而導致回滾 所以訂單不存在
//                //訂單已經被取消 才能解鎖庫存
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
//            //  無需解鎖
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
            //TODO 遠程查詢 sku 名字, 如果失敗 整個事務不 rollback
            //TODO 還有什麼方法讓事務不 rollback 呢
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
     * 為某訂單鎖定庫存
     * @param wareSkuLockVo
     * @return
     *
     * 庫存解鎖場景
     *  下訂單成功 訂單過期沒有支付被系統自動取消 被用戶手動取消 都要鎖定庫存
     *
     *  下訂單成功 庫存鎖定成功 但接下來的業務調用失敗導致訂單回滾 這時之前鎖定的庫存就要自動解鎖
     */
//    @Transactional(rollbackFor = NoStockException.class)
    @Transactional
    @Override
    public Boolean orderLockStock(WareSkuLockVo wareSkuLockVo) {
        /**
         * 保存庫存工作單的詳情
         * 追溯
         */
        WareOrderTaskEntity wareOrderTaskEntity = new WareOrderTaskEntity();
        wareOrderTaskEntity.setOrderSn(wareSkuLockVo.getOrderSn());
        wareOrderTaskService.save(wareOrderTaskEntity);


        //按照下單的收貨地址 找到一個就近倉庫 鎖定庫存(不做)

        //找到每個商品在哪個倉庫都有庫存
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
            //如果每一個商品都鎖定成功 將當前商品鎖定了幾件的工作單紀錄發給 MQ
            //鎖定失敗 前面保存的工作單訊息就回滾了 發送出去的消息即使要解鎖紀錄 由於在資料庫查不到 id 所以也就不會解鎖
            //
            for (Long wareId : wareIds) {
                //成功返回 1 否則是 0
                Long count = baseMapper.lockSkuStock(skuId, wareId, hasStock.getNum());
                if(count == 1) {
                    skuStockLocked = true;
                    //TODO 告訴 MQ 庫存鎖定成功
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
                    //只發 id 不行 防止回滾以後找不到數據
                    stockLockedTo.setDetail(stockDetailTo);
                    rabbitTemplate.convertAndSend("stock-event-exchange", "stock.locked", stockLockedTo);
                    break;
                } else {
                    //當前倉庫鎖定失敗
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

        //查詢資料庫關於這個訂單的鎖庫存訊息
        //如果有找到 只能證明庫存鎖定成功
        //解鎖 訂單情況
        //  沒有這個訂單 必須解鎖
        //  有這個訂單
        //      訂單狀態已取消就解鎖庫存
        //      沒取消就不能解鎖庫存

        //如果沒有找到 庫存鎖定失敗 庫存回滾了 這種情況無需解鎖
        WareOrderTaskDetailEntity wareOrderTaskDetailEntity = wareOrderTaskDetailService.getById(detailId);
        if(wareOrderTaskDetailEntity != null) {
            Long id = stockLockedTo.getId();
            WareOrderTaskEntity wareOrderTaskEntity = wareOrderTaskService.getById(id);
            String orderSn = wareOrderTaskEntity.getOrderSn();
            R r = orderFeignService.getOrderStatus(orderSn);
            if(r.getCode() == 0) {
                OrderVo orderVo = r.getData(new TypeReference<OrderVo>() {});

                //產生訂單流程因為發生錯誤而導致回滾 所以訂單不存在
                //訂單已經被取消 才能解鎖庫存
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
                throw new RuntimeException("遠程服務失敗");
//                channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
            }
        } else {
            //  無需解鎖
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    //防止訂單服務卡頓 導致訂單狀態消息一直改不了 庫存消息優先到期 查訂單狀態新建狀態 什麼都不做就走了
    //導致卡頓的訂單 永遠都不能解鎖庫存
    @Transactional
    @Override
    public void unlockStock(OrderTo orderTo) {
        String orderSn = orderTo.getOrderSn();
        //查一下最新庫存的狀態 防止重複解鎖庫存
        WareOrderTaskEntity wareOrderTaskEntity = wareOrderTaskService.getOrderTaskByOrderSn(orderSn);
        Long wareOrderTaskId = wareOrderTaskEntity.getId();
        //按照工作單找到所有沒有解鎖的庫存 進行解鎖
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