package com.practice.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

//        "properties": {
//            "skuId": {
//                "type": "long"
//            },
//            "spuId": {
//                "type": "keyword"
//            },
//            "skuTitle": {
//                "type": "text",
//                        "analyzer": "ik_smart"
//            },
//            "skuPrice": {
//                "type": "keyword"
//            },
//            "skuImg": {
//                "type": "keyword"
//            },
//            "saleCount": {
//                "type": "long"
//            },
//            "hasStock": {
//                "type": "boolean"
//            },
//            "hotScore": {
//                "type": "long"
//            },
//            "brandId": {
//                "type": "long"
//            },
//            "catelogId": {
//                "type": "long"
//            },
//            "brandName": {
//                "type": "keyword"
//            },
//            "brandImg": {
//                "type": "keyword"
//            },
//            "catelogName": {
//                "type": "keyword"
//            },
//            "attrs": {
//                "type": "nested",
//                        "properties": {
//                    "attrId": {
//                        "type": "long"
//                    },
//                    "attrName": {
//                        "type": "keyword"
//                    },
//                    "attrValue": {
//                        "type": "keyword"
//                    }
//                }
//            }
//        }

@Data
public class SkuEsModel {

    private Long skuId;
    private Long spuId;
    private String skuTitle;
    private BigDecimal skuPrice;
    private String skuImg;
    private Long saleCount;
    private Boolean hasStock;
    private Long hotScore;
    private Long brandId;
    private Long catalogId;
    private String brandName;
    private String brandImg;
    private String catalogName;
    private List<Attrs> attrs;

    @Data
    public static class Attrs{
        private Long attrId;
        private String attrName;
        private String attrValue;
    }
}
