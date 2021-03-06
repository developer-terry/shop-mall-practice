package com.practice.shopmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.practice.common.constant.ProductConstant;
import com.practice.common.to.SkuEsModel;
import com.practice.common.utils.PageUtils;
import com.practice.common.utils.Query;
import com.practice.common.utils.R;
import com.practice.shopmall.product.dao.SpuInfoDao;
import com.practice.shopmall.product.entity.*;
import com.practice.shopmall.product.feign.CouponFeignService;
import com.practice.shopmall.product.feign.SearchFeignService;
import com.practice.shopmall.product.feign.WareFeignService;
import com.practice.shopmall.product.service.*;
import com.practice.shopmall.product.to.SkuReductionTo;
import com.practice.shopmall.product.to.SpuBoundTo;
import com.practice.shopmall.product.vo.*;
//import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    SpuImagesService spuImagesService;

    @Autowired
    AttrService attrService;

    @Autowired
    ProductAttrValueService productAttrValueService;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    WareFeignService wareFeignService;

    @Autowired
    SearchFeignService searchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    //TODO ??????????????????
//    @GlobalTransactional
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo spuSaveVo) {

        //?????????????????? spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(spuInfoEntity);

        //??????SPU??????????????? spu_info_desc
        List<String> descripts = spuSaveVo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",", descripts));
        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);

        //??????SPU????????? spu_images
        //TODO ??????SPU?????????
        List<String> images = spuSaveVo.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(), images);

        //??????SPU??????????????? product_attr_value
        List<BaseAttrs> baseAttrs = spuSaveVo.getBaseAttrs();
        List<ProductAttrValueEntity> productAttrValueEntities = baseAttrs.stream().map((baseAttr) -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setAttrId(baseAttr.getAttrId());
            AttrEntity attrEntity = attrService.getById(baseAttr.getAttrId());
            productAttrValueEntity.setAttrName(attrEntity.getAttrName());
            productAttrValueEntity.setAttrValue(baseAttr.getAttrValues());
            productAttrValueEntity.setQuickShow(baseAttr.getShowDesc());
            productAttrValueEntity.setSpuId(spuInfoEntity.getId());

            return productAttrValueEntity;
        }).collect(Collectors.toList());

        productAttrValueService.saveProductAttr(productAttrValueEntities);

        //??????SPU??????????????? sms_spu_bounds
        Bounds bounds = spuSaveVo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds, spuBoundTo);
        spuBoundTo.setSpuId(spuInfoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundTo);
        if(r.getCode() != 0) {
//            log.error("???????????? spu ??????????????????");
        }

        //????????????SPU???????????????SKU??????
        List<Skus> skus = spuSaveVo.getSkus();
        if(skus != null && skus.size() > 0) {
            skus.forEach((sku) -> {

                String defaultImg = "";
                for (Images img : sku.getImages()) {
                    if(img.getDefaultImg() == 1) {
                        defaultImg = img.getImgUrl();
                    }
                }

                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(sku, skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                // SKU ???????????? sku_info
                skuInfoService.saveSkuInfo(skuInfoEntity);

                Long skuId = skuInfoEntity.getSkuId();

                List<SkuImagesEntity> skuImagesEntities = sku.getImages().stream().map(image -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(image.getImgUrl());
                    skuImagesEntity.setDefaultImg(image.getDefaultImg());
                    return skuImagesEntity;
                }).filter(image -> {
                    return !StringUtils.isEmpty(image.getImgUrl());
                }).collect(Collectors.toList());

                // SKU ???????????? sku_images
                skuImagesService.saveBatch(skuImagesEntities);

                List<Attr> attr = sku.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream().map(item -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(item, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuId);
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());

                // SKU ????????????????????? sku_sale_attr_value
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

                // SKU ??????????????? sms_sku_ladder, sms_sku_full_
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(sku, skuReductionTo);
//                skuReductionTo.setMemberPrice(sku.getMemberPrice());

                skuReductionTo.setSkuId(skuId);
                if(skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal(0)) == 1) {
                    R r2 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if(r2.getCode() != 0) {
//                        log.error("???????????? spu ??????????????????");
                    }
                }
            });
        }
    }

    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }

    @Override
    public PageUtils queryPageCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<SpuInfoEntity>();

        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)) {
            queryWrapper.and(wrapper -> {
                wrapper.eq("id", key).or().like("spu_name", key);
            });
        }

        String status = (String) params.get("status");
        if(!StringUtils.isEmpty(status)) {
            queryWrapper.eq("publish_status", status);
        }

        String brandId = (String) params.get("brandId");
//        System.out.println(brandId);
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!");
//        System.out.println(!StringUtils.isEmpty(brandId));
        if(!StringUtils.isEmpty(brandId) && !"0".equals(brandId)) {
            queryWrapper.eq("brand_id", brandId);
        }

        String catelogId = (String) params.get("catelogId");
        if(!StringUtils.isEmpty(catelogId) && !"0".equals(catelogId)) {
            queryWrapper.eq("catalog_id", catelogId);
        }

        IPage<SpuInfoEntity> page = this.page(new Query<SpuInfoEntity>().getPage(params), queryWrapper);

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void up(Long spuId) {
        SkuEsModel esModel = new SkuEsModel();

        List<SkuInfoEntity> skuInfoEntities = skuInfoService.getSkusBySpuId(spuId);
        List<Long> skuIds = skuInfoEntities.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());

        Map<Long, Boolean> skuHasStockVoMap = null;
        try {
            R r = wareFeignService.getSkusHasStock(skuIds);
//            TypeReference<List<SkuHasStockVo>> typeReference = new TypeReference<List<SkuHasStockVo>>(){};
//            skuHasStockVoMap = r.getData(typeReference).stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, item -> item.getHasStock()));
//            skuHasStockVoMap = (LinkedHashMap) r.get("data");
        } catch (Exception e) {
//            log.error("???????????????????????????{}", e);
        }

        //TODO ???????????? sku ?????????????????????????????????????????????
        List<ProductAttrValueEntity> productAttrValueEntities = productAttrValueService.baseAttrListForSpu(spuId);
        List<Long> attrIds = productAttrValueEntities.stream().map(productAttrValueEntity -> {
            return productAttrValueEntity.getAttrId();
        }).collect(Collectors.toList());
        System.out.println(attrIds);
        List<Long> searchAttrIds = attrService.selectSearchAttrs(attrIds);
        Set<Long> idSet = new HashSet<>(searchAttrIds);

        List<SkuEsModel.Attrs> attrs = new ArrayList<>();

        List<SkuEsModel.Attrs> attrsList = productAttrValueEntities.stream().filter(productAttrValueEntity -> {
            return idSet.contains(productAttrValueEntity.getAttrId());
        }).map(productAttrValueEntity -> {
            SkuEsModel.Attrs attrs1 = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(productAttrValueEntity, attrs1);
            return attrs1;
        }).collect(Collectors.toList());

        //??????sku info
        Map<Long, Boolean> finalSkuHasStockVoMap = skuHasStockVoMap;
        List<SkuEsModel> skuEsModelList = skuInfoEntities.stream().map(skuInfoEntity -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(skuInfoEntity, skuEsModel);

            //skuPrice skuImg
            skuEsModel.setSkuPrice(skuInfoEntity.getPrice());
            skuEsModel.setSkuImg(skuInfoEntity.getSkuDefaultImg());
            //hasStock,hotScore
            //TODO ?????????????????? ?????????????????????????????????
            if(finalSkuHasStockVoMap == null) {
                skuEsModel.setHasStock(true);
            } else {
                skuEsModel.setHasStock(finalSkuHasStockVoMap.get(skuInfoEntity.getSkuId()));
            }

            //TODO ????????????
            skuEsModel.setHotScore(0L);

            //TODO ???????????????????????????
            BrandEntity brandEntity = brandService.getById(skuEsModel.getBrandId());
            skuEsModel.setBrandName(brandEntity.getName());
            skuEsModel.setBrandImg(brandEntity.getLogo());

            CategoryEntity categoryEntity = categoryService.getById(skuEsModel.getCatalogId());
            skuEsModel.setCatalogName(categoryEntity.getName());

            skuEsModel.setAttrs(attrsList);
            //brandName,brandImg,categoryName,attrs

            return skuEsModel;
        }).collect(Collectors.toList());

        //TODO ?????????????????? es ????????????
        R r = searchFeignService.productStatusUp(skuEsModelList);
        System.out.println(r.getCode());
        System.out.println("======================");
        if(r.getCode() == 0) {
            //??????
            //TODO ???????????? spu ??????
            System.out.println(spuId);
            System.out.println("======================");
            System.out.println(ProductConstant.StatusEnum.SPU_UP.getCode());
            baseMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
        } else {
            //??????
            //TODO ???????????? ???????????????
            /**
             * ?????????????????? ??????????????? json
             *  RequestTemplate template = buildTemplateFromArgs.create(argv);
             * ???????????????????????? (?????????????????????????????????)
             *  executeAndDecode(template)
             * ??????????????????????????????
             *  while(true) {
             *      try{
             *          executeAndDecode(template);
             *      } catch (Exception e) {
             *          try{
             *              retryer.continueOrPropagate(e);
             *          } catch (Exception e) {
             *              throw ex;
             *          }
             *          continue;
             *      }
             *  }
             */
        }
    }

    @Override
    public SpuInfoEntity getSpuInfoBySkuId(Long skuId) {
        SkuInfoEntity skuInfoEntity = skuInfoService.getById(skuId);
        Long spuId = skuInfoEntity.getSpuId();
        SpuInfoEntity spuInfoEntity = getById(spuId);
        return spuInfoEntity;
    }
}
