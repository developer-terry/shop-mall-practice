package com.practice.shopmall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderSubmitVo {
    private Long addrId;
    private Integer payType;

    //無需提交需要購買的商品, 到購物車再獲取一次

    private String orderToken;
    private BigDecimal payPrice; // 應付價格 驗價

    //用戶相關信息

    private String note;
}
