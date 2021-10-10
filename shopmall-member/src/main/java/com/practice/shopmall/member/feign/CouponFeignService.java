package com.practice.shopmall.member.feign;

import com.practice.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("shopmall-coupon")
public interface CouponFeignService {

    @RequestMapping("coupon/coupon/member/coupons")
    public R memberCoupons();
}
