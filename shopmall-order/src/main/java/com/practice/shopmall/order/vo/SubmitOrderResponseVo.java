package com.practice.shopmall.order.vo;

import com.practice.shopmall.order.entity.OrderEntity;
import lombok.Data;

@Data
public class SubmitOrderResponseVo {
    private OrderEntity order;
    private Integer code; // 0:success
}
