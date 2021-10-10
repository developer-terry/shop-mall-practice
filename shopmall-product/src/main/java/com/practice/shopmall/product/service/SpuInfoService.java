package com.practice.shopmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.practice.common.utils.PageUtils;
import com.practice.shopmall.product.entity.SpuInfoDescEntity;
import com.practice.shopmall.product.entity.SpuInfoEntity;
import com.practice.shopmall.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author TerryLee
 * @email p134030772@gmail.com
 * @date 2021-06-10 23:59:44
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo spuSaveVo);

    void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity);

    PageUtils queryPageCondition(Map<String, Object> params);

    void up(Long spuId);

    SpuInfoEntity getSpuInfoBySkuId(Long skuId);
}

