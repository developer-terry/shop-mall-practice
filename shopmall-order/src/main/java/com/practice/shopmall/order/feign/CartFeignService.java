package com.practice.shopmall.order.feign;

import com.practice.shopmall.order.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient("shopmall-cart")
public interface CartFeignService {

    @GetMapping("currentUserCartItems")
    public List<OrderItemVo> getCurrentUserCartItems();
}
