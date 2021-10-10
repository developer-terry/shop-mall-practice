package com.practice.shopmall.ware.vo;

import lombok.Data;

import java.util.List;

@Data
public class WareSkuLockVo {
    private String orderSn;
    private List<OrderItemVo> locks; //需要鎖住的所有庫存訊息
}
