package com.practice.shopmall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * 封裝頁面所有可能傳遞過來的查詢條件
 * sort=
 * saleCount_asc
 * saleCount_desc
 */
@Data
public class SearchParam {

    private String keyword; //全文匹配關鍵字
    private Long catalog3Id;
    /**
     * saleCount_asc
     * saleCount_desc
     * skuPrice_asc
     * skuPrice_desc
     * hotScore_asc
     * hotScore_desc
     */
    private String sort;

    /**
     * 各種過濾條件
     * hasStock 是否有貨 0 1
     * skuPrice 區間 1_500 _500 500_
     * brandId 品牌 brandId=1&brandId=2&brandId=3
     * attrs 屬性 attrs=1_安卓:頻果&attrs=2_2.5寸:5寸
     */

    private Integer hasStock;
    private String skuPrice;
    private List<Long> brandId;
    private List<String> attrs;
    private Integer pageNumber = 1;

    private String _queryString;
}
