package com.practice.shopmall.product.service.impl;

import com.practice.shopmall.product.vo.SkuItemSaleAttrVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.practice.common.utils.PageUtils;
import com.practice.common.utils.Query;

import com.practice.shopmall.product.dao.SkuSaleAttrValueDao;
import com.practice.shopmall.product.entity.SkuSaleAttrValueEntity;
import com.practice.shopmall.product.service.SkuSaleAttrValueService;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuItemSaleAttrVo> getSaleAttrsBySpuId(Long spuId) {
        List<SkuItemSaleAttrVo> saleAttrVoList = this.baseMapper.getSaleAttrsBySpuId(spuId);
        return saleAttrVoList;
    }

    @Override
    public List<String> getSkuSaleAttrValuesAsStringList(Long skuId) {
        System.out.println(baseMapper.getSkuSaleAttrValuesAsStringList(skuId));
        System.out.println("===================================");
        return baseMapper.getSkuSaleAttrValuesAsStringList(skuId);
    }

}