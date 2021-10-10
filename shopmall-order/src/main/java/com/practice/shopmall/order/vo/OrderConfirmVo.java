package com.practice.shopmall.order.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class OrderConfirmVo {

    @Setter @Getter
    List<MemberRecieveAddressVo> addresses;

    @Setter @Getter
    List<OrderItemVo> items;

    @Setter @Getter
    Integer integration;

    @Setter @Getter
    Map<Long, Boolean> stocks;

    //防重令牌
    @Setter @Getter
    String orderToken;

    public Integer getCount(){
        Integer count = 0;
        if(items != null) {
            for (OrderItemVo item : items) {
                count += item.getCount();
            }
        }

        return count;
    }

//    BigDecimal total; //訂單總額
//    BigDecimal payPrice; //應付價格

    public BigDecimal getTotal(){

        BigDecimal total = new BigDecimal("0");

        if(items != null) {
            for (OrderItemVo item : items) {
                BigDecimal subTotal = item.getPrice().multiply(new BigDecimal(item.getCount().toString()));
                total = total.add(subTotal);
            }
        }

        return total;
    }

    public BigDecimal getPayPrice(){

        BigDecimal payPrice = new BigDecimal("0");

        if(items != null) {
            for (OrderItemVo item : items) {
                BigDecimal subTotal = item.getPrice().multiply(new BigDecimal(item.getCount().toString()));
                payPrice = payPrice.add(subTotal);
            }
        }

        return payPrice;
    }
}
