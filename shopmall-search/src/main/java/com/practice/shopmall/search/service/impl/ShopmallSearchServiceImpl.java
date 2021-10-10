package com.practice.shopmall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.practice.common.to.SkuEsModel;
import com.practice.common.utils.R;
import com.practice.shopmall.search.config.ShopmallElasticSearchConfig;
import com.practice.shopmall.search.constant.EsConstant;
import com.practice.shopmall.search.feign.ProductFeignService;
import com.practice.shopmall.search.service.ShopmallSearchService;
import com.practice.shopmall.search.vo.AttrResponseVo;
import com.practice.shopmall.search.vo.BrandVo;
import com.practice.shopmall.search.vo.SearchParam;
import com.practice.shopmall.search.vo.SearchResult;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopmallSearchServiceImpl implements ShopmallSearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ProductFeignService productFeignService;

    @Override
    public SearchResult search(SearchParam searchParam) {

        SearchResult searchResult = null;

        //準備檢索請求
        SearchRequest searchRequest = buildSearchRequest(searchParam);

        try {
            //執行檢索請求
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, ShopmallElasticSearchConfig.COMMON_OPTIONS);

            //分析響應數據封裝成我們需要的格式
            searchResult = buildSearchResult(searchResponse, searchParam);
            System.out.println(searchResult);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return searchResult;
    }

    private SearchRequest buildSearchRequest(SearchParam searchParam) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        /**
         * 模糊匹配 過濾 按照屬性 分類 品牌 價格區間 庫存
         */

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if(!StringUtils.isEmpty(searchParam.getKeyword())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("skuTitle", searchParam.getKeyword()));
        }

        if(searchParam.getCatalog3Id() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("catalogId", searchParam.getCatalog3Id()));
        }

        if(searchParam.getBrandId() != null && searchParam.getBrandId().size() > 0) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId", searchParam.getBrandId()));
        }

        if(searchParam.getBrandId() != null && searchParam.getBrandId().size() > 0) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId", searchParam.getBrandId()));
        }

        if(searchParam.getAttrs() != null && searchParam.getAttrs().size() > 0) {

            for (String attrStr : searchParam.getAttrs()) {
                BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();

                String[] s = attrStr.split("_");
                String attrId = s[0];
                String[] attrValues = s[1].split(":");
                nestedBoolQueryBuilder.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                nestedBoolQueryBuilder.must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));

                NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("attrs", nestedBoolQueryBuilder, ScoreMode.None);
                boolQueryBuilder.filter(nestedQueryBuilder);
            }
        }

        if(searchParam.getHasStock() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("hasStock", searchParam.getHasStock() == 1));
        }

        if(!StringUtils.isEmpty(searchParam.getSkuPrice())) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("skuPrice");
            String[] s = searchParam.getSkuPrice().split("_");

            if(s.length == 2) {
                rangeQueryBuilder.gte(s[0]).lte(s[1]);
            } else if(s.length == 1) {
                if(searchParam.getSkuPrice().startsWith("_")) {
                    rangeQueryBuilder.lte(s[0]);
                }

                if(searchParam.getSkuPrice().endsWith("_")) {
                    rangeQueryBuilder.gte(s[0]);
                }
            }

            boolQueryBuilder.filter(rangeQueryBuilder);
        }

        sourceBuilder.query(boolQueryBuilder);

        /**
         * 排序 分頁 高亮
         */
        if(!StringUtils.isEmpty(searchParam.getSort())) {
            String sort = searchParam.getSort();

            String[] s = sort.split("_");
            SortOrder order = "asc".equalsIgnoreCase(s[1]) ? SortOrder.ASC : SortOrder.DESC;
            sourceBuilder.sort(s[0], order);
        }

        sourceBuilder.from((searchParam.getPageNumber() - 1) * EsConstant.PRODUCT_PAGE_SIZE);
        sourceBuilder.size(EsConstant.PRODUCT_PAGE_SIZE);

        if(!StringUtils.isEmpty(searchParam.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            sourceBuilder.highlighter(highlightBuilder);
        }

        System.out.println("構建的 DSL");
        System.out.println(sourceBuilder.toString());

        /**
         * 聚合分析
         */

        //TODO brand
        TermsAggregationBuilder brandAgg = AggregationBuilders.terms("brand_agg").field("brandId").size(50);

        brandAgg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        brandAgg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));

        sourceBuilder.aggregation(brandAgg);

        //TODO catalog
        TermsAggregationBuilder catalogAgg = AggregationBuilders.terms("catalog_agg").field("catalogId").size(20);
        catalogAgg.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(1));

        sourceBuilder.aggregation(catalogAgg);

        //TODO attr
        NestedAggregationBuilder nestedAttrAgg = AggregationBuilders.nested("attr_agg", "attrs");
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        attrIdAgg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1));
        attrIdAgg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(50));
        nestedAttrAgg.subAggregation(attrIdAgg);
        sourceBuilder.aggregation(nestedAttrAgg);

        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, sourceBuilder);
        return searchRequest;
    }

    private SearchResult buildSearchResult(SearchResponse searchResponse, SearchParam searchParam) {

        SearchResult searchResult = new SearchResult();

        SearchHits hits = searchResponse.getHits();

        List<SkuEsModel> skuEsModelList = new ArrayList<>();
        if(hits.getHits() != null && hits.getHits().length > 0) {
            for (SearchHit hit : hits.getHits()){
                String sourceAsString = hit.getSourceAsString();
                SkuEsModel skuEsModel = JSON.parseObject(sourceAsString, SkuEsModel.class);

                if(!StringUtils.isEmpty(searchParam.getKeyword())){
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    String skuTitleString = skuTitle.getFragments()[0].string();
                    skuEsModel.setSkuTitle(skuTitleString);
                }

                skuEsModelList.add(skuEsModel);
            }
        }

        searchResult.setProducts(skuEsModelList);

        //
        List<SearchResult.AttrVo> attrVoList = new ArrayList<>();

        ParsedNested attrAgg = searchResponse.getAggregations().get("attr_agg");
        ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attr_id_agg");
        List<? extends Terms.Bucket> attrIdBuckets = attrIdAgg.getBuckets();
        for (Terms.Bucket bucket : attrIdBuckets) {
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
            Long attrId = bucket.getKeyAsNumber().longValue();
            String attrName = ((ParsedStringTerms)bucket.getAggregations().get("attr_name_agg")).getBuckets().get(0).getKeyAsString();
            List<String> attrValues = ((ParsedStringTerms)bucket.getAggregations().get("attr_value_agg")).getBuckets().stream().map(bucket1 -> {
                return ((Terms.Bucket)bucket1).getKeyAsString();
            }).collect(Collectors.toList());

            attrVo.setAttrId(attrId);
            attrVo.setAttrName(attrName);
            attrVo.setAttrValues(attrValues);

            attrVoList.add(attrVo);
        }

        searchResult.setAttrs(attrVoList);

        //
        List<SearchResult.BrandVo> brandVoList = new ArrayList<>();

        ParsedLongTerms brandAgg = searchResponse.getAggregations().get("brand_agg");
        List<? extends Terms.Bucket> brandBuckets = brandAgg.getBuckets();
        for (Terms.Bucket bucket : brandBuckets) {
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();
            Long brandId = bucket.getKeyAsNumber().longValue();
            String brandName = ((ParsedStringTerms)bucket.getAggregations().get("brand_name_agg")).getBuckets().get(0).getKeyAsString();
            ParsedStringTerms brandImgAgg = bucket.getAggregations().get("brand_img_agg");
            String brandImg = "";
            if(brandImgAgg.getBuckets().size() > 0) {
                brandImg = brandImgAgg.getBuckets().get(0).getKeyAsString();
            }

            brandVo.setBrandId(brandId);
            brandVo.setBrandName(brandName);
            brandVo.setBrandImg(brandImg);
            brandVoList.add(brandVo);
        }

        searchResult.setBrands(brandVoList);

        //
        List<SearchResult.CatalogVo> catalogVoList = new ArrayList<>();

        ParsedLongTerms catalogAgg = searchResponse.getAggregations().get("catalog_agg");
        List<? extends Terms.Bucket> catalogBuckets = catalogAgg.getBuckets();
        for (Terms.Bucket bucket : catalogBuckets) {
            String keyAsString = bucket.getKeyAsString();
            ParsedStringTerms catalogNameAgg = bucket.getAggregations().get("catalog_name_agg");
            String catalogName = catalogNameAgg.getBuckets().get(0).getKeyAsString();

            SearchResult.CatalogVo catalogVo = new SearchResult.CatalogVo();
            catalogVo.setCatalogId(Long.parseLong(keyAsString));
            catalogVo.setCatalogName(catalogName);

            catalogVoList.add(catalogVo);
        }

        searchResult.setCatalogs(catalogVoList);

        searchResult.setPageNumber(searchParam.getPageNumber());

        Long total = hits.getTotalHits().value;
        searchResult.setTotal(total);

        int totalPages = total.intValue() % EsConstant.PRODUCT_PAGE_SIZE == 0 ? total.intValue() / EsConstant.PRODUCT_PAGE_SIZE : (total.intValue() / EsConstant.PRODUCT_PAGE_SIZE + 1);
        searchResult.setTotalPages(totalPages);

        List<Integer> pageNavs = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pageNavs.add(i);
        }

        searchResult.setPageNavs(pageNavs);

        List<SearchResult.NavVo> navs = new ArrayList<>();
        System.out.println("--------------------------------");
        System.out.println(searchParam.getAttrs());
        System.out.println(searchParam);
        System.out.println("--------------------------------");

        if(searchParam.getAttrs() != null && searchParam.getAttrs().size() > 0) {
            navs = searchParam.getAttrs().stream().map(attr -> {
                SearchResult.NavVo navVo = new SearchResult.NavVo();

                String[] s = attr.split("_");

                R r = productFeignService.attrInfo(Long.parseLong(s[0]));

                searchResult.getAttrIds().add(Long.parseLong(s[0]));

                if(r.getCode() == 0) {
                    AttrResponseVo attrResponseVo = r.getData("attr", new TypeReference<AttrResponseVo>(){});
                    navVo.setNavName(attrResponseVo.getAttrName());
                } else {
                    navVo.setNavName(s[0]);
                }

                navVo.setNavValue(s[1]);

                String replace = replaceQueryString(searchParam, "attrs", attr);
                navVo.setLink("http://search.shopmall-test.com/list.html?" + replace);

                return navVo;
            }).collect(Collectors.toList());

            searchResult.setNavs(navs);
        }

        if(searchParam.getBrandId() != null && searchParam.getBrandId().size() > 0) {
            List<SearchResult.NavVo> navs1 = searchResult.getNavs();
            SearchResult.NavVo navVo = new SearchResult.NavVo();

            R r = productFeignService.brandInfo(searchParam.getBrandId());
            if(r.getCode() == 0) {
                List<BrandVo> brands = r.getData("brands", new TypeReference<List<BrandVo>>(){});
                StringBuffer buffer = new StringBuffer();
                String replace = "";

                for (BrandVo brandVo : brands) {
                    buffer.append(brandVo.getBrandName() + ";");
                    replace = replaceQueryString(searchParam, "brandId", brandVo.getBrandId() + "");
                }

                navVo.setNavValue(buffer.toString());
                navVo.setLink("http://search.shopmall-test.com/list.html?" + replace);
            }
        }

        System.out.println(searchResult);
        return searchResult;
    }

    private String replaceQueryString(SearchParam searchParam, String key, String value) {
        String encode = null;
        try {
            encode = URLEncoder.encode(value, "UTF-8");
            encode.replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String replace = searchParam.get_queryString().replace(key + "=" + encode, "");
        return replace;
    }


}
