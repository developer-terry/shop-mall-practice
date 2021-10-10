package com.practice.shopmall.cart.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Cart {
    List<CartItem> items;
    private Integer countNum;
    private Integer countType;
    private BigDecimal totalAmount; //商品總價
    private BigDecimal reduce = new BigDecimal("0"); //折價

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public Integer getCountNum() {
        int count = 0;
        if(items != null && items.size() > 0) {
            for (CartItem item : items) {
                count += item.getCount();
            }
        }
        return count;
    }

    public Integer getCountType() {
        int count = 0;

        if(items != null) {
            count = items.size();
        }

        return count;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal amount = new BigDecimal("0");
        if(items != null && items.size() > 0) {
            for (CartItem item : items) {
                if(item.getCheck()) {
                    amount = amount.add(item.getTotalPrice());
                }
            }
        }

        BigDecimal subtract = amount.subtract(reduce);

        return subtract;
    }

    public BigDecimal getReduce() {
        return reduce;
    }

    public void setReduce(BigDecimal reduce) {
        this.reduce = reduce;
    }
}
