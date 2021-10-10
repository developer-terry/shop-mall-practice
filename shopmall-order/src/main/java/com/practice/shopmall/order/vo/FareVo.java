package com.practice.shopmall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FareVo {
    private MemberRecieveAddressVo address;
    private BigDecimal fare;
}
