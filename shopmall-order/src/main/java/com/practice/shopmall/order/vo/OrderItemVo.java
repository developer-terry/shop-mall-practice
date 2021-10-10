package com.practice.shopmall.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderItemVo {
    private Long skuId;
    private String title;
    private String image;
    private List<String> skuAttr;
    private BigDecimal price;
    private Integer count;
    private BigDecimal totalPrice;

//    //TODO 查詢庫存狀態
//    private Boolean hasStock;
    private BigDecimal weight;
}
