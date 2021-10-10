package com.practice.shopmall.product.fallback;

import com.practice.common.utils.R;
import com.practice.shopmall.product.feign.SeckillFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SeckillFeignServiceFallBack implements SeckillFeignService {

    @Override
    public R skuSeckillInfo(Long skuId) {
        log.info("熔斷方法調用");
        return R.error("太多請求阻塞");
    }
}
