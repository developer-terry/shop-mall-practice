package com.practice.shopmall.product.feign;

import com.practice.common.utils.R;
import com.practice.shopmall.product.to.SkuReductionTo;
import com.practice.shopmall.product.to.SpuBoundTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("shopmall-coupon")
public interface CouponFeignService {

    /**
     * CouponFeignService.saveSpuBounds(spuBoundTo);
     *  將對象轉為 json
     *  找到 shopmall-coupon 遠程服務,給 /counpon/spubounds/save 發送請求,
     *  將上一步轉的 json 放在請求體位置, 發送請求
     *  對方服務收到請求, 請求體裡有 json 數據
     *  (@RequestBody SpuBoundsEntity spuBounds), 將請求體的 json 轉為 spuBoundsEntity
     * 只要 json 數據模型是兼容的, 雙方服務無需使用同一個 vo
     * @param spuBoundTo
     * @return
     */

    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
