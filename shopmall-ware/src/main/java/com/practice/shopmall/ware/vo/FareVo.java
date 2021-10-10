package com.practice.shopmall.ware.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FareVo {
    private MemberRecieveAddressVo address;
    private BigDecimal fare;
}
