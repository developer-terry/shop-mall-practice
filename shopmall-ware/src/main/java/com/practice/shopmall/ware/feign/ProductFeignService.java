package com.practice.shopmall.ware.feign;

import com.practice.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("shopmall-gateway")
public interface ProductFeignService {

    /**
     * 讓所有請求過網關
     *      @FeignClient("shopmall-gateway") 給 gateway 所在的機器發請求
     *      api/product/skuinfo/info/{skuId}
     * 直接讓後台指定服務處理
     *      @FeignClient("shopmall-product")
     *      /product/skuinfo/info/{skuId}
     *
     * @param skuId
     * @return
     */
    @RequestMapping("product/skuinfo/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);
}
