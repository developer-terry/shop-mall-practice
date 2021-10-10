package com.practice.shopmall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CatelogSecondVo {
    private String catalog1Id;
    private List<CatelogThirdVo> catalog3List;
    private String id;
    private String name;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class CatelogThirdVo {
        private String catalog2Id;
        private String id;
        private String name;
    }
}
