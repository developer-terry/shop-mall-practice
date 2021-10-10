package com.practice.shopmall.ware.vo;

import lombok.Data;

@Data
public class LockStockResult {
    private Long skuId;
    private Integer stock;
    private Boolean locked;
}
