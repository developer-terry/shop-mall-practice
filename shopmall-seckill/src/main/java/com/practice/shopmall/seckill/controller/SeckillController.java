package com.practice.shopmall.seckill.controller;

import com.practice.common.utils.R;
import com.practice.shopmall.seckill.service.SeckillService;
import com.practice.shopmall.seckill.to.SeckillSkuRedisTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.model.IModel;

import java.util.List;

@Slf4j
@Controller
public class SeckillController {

    @Autowired
    SeckillService seckillService;

    //返回當前時間可以參與的秒殺商品訊息
    @ResponseBody
    @GetMapping("/currentSeckillSkus")
    public R getCurrentSeckillSkus() {
        List<SeckillSkuRedisTo> seckillSkuRedisToList = seckillService.getCurrentSeckillSkus();
        return R.ok().setData(seckillSkuRedisToList);
    }

    @ResponseBody
    @GetMapping("/sku/seckill/{skuId}")
    public R skuSeckillInfo(@PathVariable("skuId") Long skuId){
        long time = System.currentTimeMillis();
        SeckillSkuRedisTo seckillSkuRedisTo = seckillService.skuSeckillInfo(skuId);
        long time2 = System.currentTimeMillis();
        log.info("取得秒殺；" + (time2 - time));
        return R.ok().setData(seckillSkuRedisTo);
    }

    @GetMapping("/kill")
    public String seckill(
            @RequestParam("killId") String killId,
            @RequestParam("key") String key,
            @RequestParam("num") Integer num,
            Model model) {

        String orderSn = seckillService.kill(killId, key, num);
        model.addAttribute("orderSn", orderSn);
        return "success";
    }
}
