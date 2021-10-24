package com.practice.shopmall.search;

import com.alibaba.fastjson.JSON;
import com.practice.shopmall.search.config.ShopmallElasticSearchConfig;
import lombok.Data;
import lombok.ToString;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class SearchApplicationTests {

	@Autowired
	RestHighLevelClient restHighLevelClient;

	@Test
	void contextLoads() {
		System.out.println(restHighLevelClient);
	}

	@Test
	void indexData() throws IOException {
		IndexRequest indexRequest = new IndexRequest("users");
		indexRequest.id("1");
//		indexRequest.source("userName", "terry", "age", 18, "gender", "男")
		User user = new User();
		user.setUserName("Terry");
		user.setAge(18);
		user.setGender("男");
		String jsonString = JSONValue.toJSONString(user);
		indexRequest.source(jsonString, XContentType.JSON);

		IndexResponse indexResponse = restHighLevelClient.index(indexRequest, ShopmallElasticSearchConfig.COMMON_OPTIONS);
		System.out.println(indexResponse);
	}

	@Test
	void searchData() throws IOException {
		SearchRequest searchRequest = new SearchRequest();

		searchRequest.indices("bank");

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		//構造檢索條件
//		searchSourceBuilder.query();
//		searchSourceBuilder.from();
//		searchSourceBuilder.size();
//		searchSourceBuilder.aggregations();

		searchSourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));

		TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age").size(10);
		searchSourceBuilder.aggregation(ageAgg);

		AvgAggregationBuilder balanceAvgAgg = AggregationBuilders.avg("balanceAvgAgg").field("balance");
		searchSourceBuilder.aggregation(balanceAvgAgg);

		System.out.println(searchSourceBuilder.toString());

		searchRequest.source(searchSourceBuilder);

		SearchResponse searchResponse = restHighLevelClient.search(searchRequest, ShopmallElasticSearchConfig.COMMON_OPTIONS);

		System.out.println(searchResponse.toString());

		SearchHits hits = searchResponse.getHits();
		SearchHit[] searchHits = hits.getHits();
		for (SearchHit hit : searchHits) {
//			hit.getIndex();
//			hit.getId();
			String hitString = hit.getSourceAsString();
			System.out.println(hitString);
			Account account = JSON.parseObject(hitString, Account.class);
			System.out.println(account);
		}

		Aggregations aggregations = searchResponse.getAggregations();
		Terms ageAgg1 = aggregations.get("ageAgg");
		for (Terms.Bucket bucket : ageAgg1.getBuckets()) {
			String keyAsString = bucket.getKeyAsString();
			System.out.println(keyAsString);
		}

		Avg balanceAgg1 = aggregations.get("balanceAvgAgg");
		System.out.println(balanceAgg1.getValue());

//		for (Aggregation aggregation: aggregations.asList()) {
//			System.out.println(aggregation.getName());
//
//		}



//		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
//		searchRequest.source(searchSourceBuilder);
	}

	@Data
	class User{
		private String userName;
		private String gender;
		private Integer age;
	}

	@ToString
	@Data
	static class Account{
		private int accountNumber;
		private int balance;
		private String firstname;
		private String lastname;
		private int age;
		private String gender;
		private String address;
		private String employer;
		private String email;
		private String city;
		private String state;
	}

}
