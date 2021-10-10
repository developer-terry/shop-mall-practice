package com.practice.shopmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.practice.common.utils.PageUtils;
import com.practice.shopmall.product.entity.CategoryEntity;
import com.practice.shopmall.product.vo.CatelogSecondVo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author TerryLee
 * @email p134030772@gmail.com
 * @date 2021-06-10 23:59:44
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    public List<CategoryEntity> listWithTree();

    void removeCategoryByIds(List<Long> asList);

    public Long[] findCatelogPath(Long catelogId);

    void updateCascade(CategoryEntity category);

    List<CategoryEntity> getFirstLevelCategories();

    Map<String, List<CatelogSecondVo>> getCatalogJson();
}

