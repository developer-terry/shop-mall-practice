package com.practice.shopmall.product.vo;

import com.practice.shopmall.product.entity.SkuImagesEntity;
import com.practice.shopmall.product.entity.SkuInfoEntity;
import com.practice.shopmall.product.entity.SpuInfoDescEntity;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
public class SkuItemVo {

    boolean hasStock = true;

    //sku 基本訊息 pms_sku_info
    SkuInfoEntity info;

    //sku 圖片訊息 pms_sku_images
    List<SkuImagesEntity> images;

    //spu 銷售屬性組合
    List<SkuItemSaleAttrVo> saleAttr;

    //獲取 spu 介紹
    SpuInfoDescEntity desc;

    //獲取 spu 規格參數訊息
    List<SpuItemAttrGroupVo> groupAttrs;

    SeckillInfoVo seckillInfo;
}
