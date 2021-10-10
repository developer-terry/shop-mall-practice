package com.practice.shopmall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.practice.common.to.SkuEsModel;
import com.practice.shopmall.search.config.ShopmallElasticSearchConfig;
import com.practice.shopmall.search.constant.EsConstant;
import com.practice.shopmall.search.service.ProductSaveService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("productSaveService")
public class ProductSaveServiceImpl implements ProductSaveService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModelList) throws IOException {
        //給 es 建立索引 product, 建立好 mapping 關係

        //給 es 保存數據
        BulkRequest bulkRequest = new BulkRequest();
        System.out.println(skuEsModelList);
        for(SkuEsModel skuEsModel : skuEsModelList) {
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());
            String soruceString = JSON.toJSONString(skuEsModel);
            indexRequest.source(soruceString, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }

        BulkResponse bulkResponses = restHighLevelClient.bulk(bulkRequest, ShopmallElasticSearchConfig.COMMON_OPTIONS);
        boolean hasFailures = bulkResponses.hasFailures();
        List<String> collect = Arrays.stream(bulkResponses.getItems()).map(item -> {
            return item.getId();
        }).collect(Collectors.toList());

        System.out.println("商品上架完成, " + collect);

        return hasFailures;
    }
}
