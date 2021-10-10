package com.practice.shopmall.search.controller;

import com.practice.common.to.SkuEsModel;
import com.practice.common.utils.R;
import com.practice.shopmall.search.service.ProductSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/search")
@RestController
public class ElasticSaveController {

    @Autowired
    ProductSaveService productSaveService;
    //上架商品
    @PostMapping("/save/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModelList){

        boolean hasFailures = true;

        try {
            hasFailures = productSaveService.productStatusUp(skuEsModelList);
        } catch (Exception e) {
            System.out.println("Elastic Save Controller 商品上架錯誤：" + e);
            return R.error();
        }
        System.out.println(hasFailures);
        System.out.println("=====================");
        if(!hasFailures) {
            return R.ok();
        }

        return R.error();
    }
}
