package com.practice.shopmall.search.vo;

import com.practice.common.to.SkuEsModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchResult {

    private List<SkuEsModel> products;
    private Long total;
    private Integer pageNumber;
    private Integer totalPages;
    private List<Integer> pageNavs;

    private List<BrandVo> brands; //當前查詢到的結果 所有涉及到的品牌
    private List<CatalogVo> catalogs; //當前查詢到的結果所有涉及到的所有分類
    private List<AttrVo> attrs; //當前查詢到的結果 所有涉及到的所有屬性

    private List<NavVo> navs = new ArrayList<>();
    private List<Long> attrIds = new ArrayList<>();

    @Data
    public static class NavVo {
        private String navName;
        private String navValue;
        private String link;
    }

    @Data
    public static class BrandVo{
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    @Data
    public static class CatalogVo{
        private Long catalogId;
        private String catalogName;
    }

    @Data
    public static class AttrVo{
        private Long attrId;
        private String attrName;
        private List<String> attrValues;
    }
}
