package com.practice.shopmall.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.practice.common.utils.R;
import com.practice.shopmall.product.entity.SkuImagesEntity;
import com.practice.shopmall.product.entity.SpuInfoDescEntity;
import com.practice.shopmall.product.feign.SeckillFeignService;
import com.practice.shopmall.product.service.*;
import com.practice.shopmall.product.vo.SeckillInfoVo;
import com.practice.shopmall.product.vo.SkuItemSaleAttrVo;
import com.practice.shopmall.product.vo.SkuItemVo;
import com.practice.shopmall.product.vo.SpuItemAttrGroupVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.practice.common.utils.PageUtils;
import com.practice.common.utils.Query;

import com.practice.shopmall.product.dao.SkuInfoDao;
import com.practice.shopmall.product.entity.SkuInfoEntity;
import org.springframework.util.StringUtils;

@Slf4j
@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    SeckillFeignService seckillFeignService;

    @Autowired
    SkuImagesService imagesService;

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    AttrGroupService attrGroupService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<SkuInfoEntity>();

        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)) {
            queryWrapper.and(wrapper -> {
                wrapper.eq("sku_id", key).or().like("sku_name", key);
            });
        }

        String catelogId = (String) params.get("catelog_id");
        if(!StringUtils.isEmpty(catelogId) && !"0".equals(catelogId)) {
            queryWrapper.eq("catelog_id", catelogId);
        }

        String brandId = (String) params.get("brand_id");
        if(!StringUtils.isEmpty(brandId) && !"0".equals(brandId)) {
            queryWrapper.eq("brand_id", brandId);
        }

        String min = (String) params.get("min");
        if(!StringUtils.isEmpty(min)) {
            try {
                queryWrapper.ge("price", min);
            } catch (Exception e) {

            }
        }

        String max = (String) params.get("max");
        if(!StringUtils.isEmpty(max) && !"0".equals(max)) {
            try {
                queryWrapper.le("price", max);
            } catch (Exception e) {

            }
        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {
        List<SkuInfoEntity> skuInfoEntities = this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        return skuInfoEntities;
    }

    @Override
    public SkuItemVo item(Long skuId) {

        SkuItemVo skuItemVo = new SkuItemVo();

        long time1 = System.currentTimeMillis();

        CompletableFuture<SkuInfoEntity> infoEntityCompletableFuture = CompletableFuture.supplyAsync(() -> {
            //sku 基本訊息 pms_sku_info
            SkuInfoEntity info = getById(skuId);
            skuItemVo.setInfo(info);
            return info;
        }, threadPoolExecutor);

        long time2 = System.currentTimeMillis();
        log.info("sku 基本訊息" + (time2 - time1));

        CompletableFuture<Void> saleAttrFuture = infoEntityCompletableFuture.thenAcceptAsync((skuInfoEntity) -> {
            //spu 銷售屬性組合
            List<SkuItemSaleAttrVo> skuItemSaleAttrVoList = skuSaleAttrValueService.getSaleAttrsBySpuId(skuInfoEntity.getSpuId());
            skuItemVo.setSaleAttr(skuItemSaleAttrVoList);
            System.out.println(skuItemSaleAttrVoList);
        }, threadPoolExecutor);

        long time3 = System.currentTimeMillis();
        log.info("spu 銷售屬性組合" + (time3 - time2));

        CompletableFuture<Void> descFuture = infoEntityCompletableFuture.thenAcceptAsync((skuInfoEntity) -> {
            //獲取 spu 介紹
            SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(skuInfoEntity.getSpuId());
            skuItemVo.setDesc(spuInfoDescEntity);
            System.out.println("spuInfoDescEntity:");
            System.out.println(spuInfoDescEntity);
        }, threadPoolExecutor);

        long time4 = System.currentTimeMillis();
        log.info("spu 介紹" + (time4 - time3));

        CompletableFuture<Void> baseAttrFuture = infoEntityCompletableFuture.thenAcceptAsync((skuInfoEntity) -> {
            //獲取 spu 規格參數訊息
            List<SpuItemAttrGroupVo> attrGroupVoList = attrGroupService.getAttrGroupWithAttrsBySpuId(skuInfoEntity.getSpuId(), skuInfoEntity.getCatalogId());
            skuItemVo.setGroupAttrs(attrGroupVoList);
        }, threadPoolExecutor);

        long time5 = System.currentTimeMillis();
        log.info("spu 規格參數訊息" + (time5 - time4));

        final CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            //sku 圖片訊息 pms_sku_images
            List<SkuImagesEntity> images = imagesService.getImagesBySkuId(skuId);
            skuItemVo.setImages(images);
        }, threadPoolExecutor);

        long time6 = System.currentTimeMillis();
        log.info("圖片訊息" + (time6 - time5));

        //查詢當前sku是否參與秒殺優惠
        CompletableFuture<Void> seckillFuture = CompletableFuture.runAsync(() -> {
            R skuSeckillInfo = seckillFeignService.skuSeckillInfo(skuId);
            if(skuSeckillInfo.getCode() == 0) {
                SeckillInfoVo seckillInfoVo = skuSeckillInfo.getData(new TypeReference<SeckillInfoVo>(){});
                skuItemVo.setSeckillInfo(seckillInfoVo);
            }
        });

        long time7 = System.currentTimeMillis();
        log.info("sku 秒殺優惠" + (time7 - time6));

        //等所有任務都完成
        try {
            CompletableFuture.allOf(
                saleAttrFuture,
                descFuture,
                baseAttrFuture,
                voidCompletableFuture,
                seckillFuture
            ).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        long time8 = System.currentTimeMillis();
        log.info("調用全部" + (time8 - time7));

        return skuItemVo;
    }

}