package com.practice.shopmall.product.web;

import com.practice.shopmall.product.service.SkuInfoService;
import com.practice.shopmall.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ItemController {

    @Autowired
    SkuInfoService skuInfoService;

    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model) {
        System.out.println("準備查詢：" + skuId);
        System.out.println("start");
        long time1 = System.currentTimeMillis();
        SkuItemVo skuItemVo = skuInfoService.item(skuId);
        model.addAttribute("item", skuItemVo);
        long time2 = System.currentTimeMillis();

        System.out.println("/{skuId}.html spend time:" + (time2 - time1));

        return "item";
    }
}
