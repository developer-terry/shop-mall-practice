package com.practice.shopmall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.practice.shopmall.product.dao.AttrGroupDao;
import com.practice.shopmall.product.dao.BrandDao;
import com.practice.shopmall.product.dao.CategoryDao;
import com.practice.shopmall.product.dao.SkuSaleAttrValueDao;
import com.practice.shopmall.product.entity.AttrEntity;
import com.practice.shopmall.product.entity.BrandEntity;
import com.practice.shopmall.product.entity.CategoryEntity;
import com.practice.shopmall.product.entity.SpuInfoEntity;
import com.practice.shopmall.product.service.AttrService;
import com.practice.shopmall.product.service.BrandService;
import com.practice.shopmall.product.service.CategoryService;
import com.practice.shopmall.product.service.SpuInfoService;
import com.practice.shopmall.product.vo.SkuItemSaleAttrVo;
import com.practice.shopmall.product.vo.SkuItemVo;
import com.practice.shopmall.product.vo.SpuItemAttrGroupVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
//import com.google.cloud.storage.BlobId;
//import com.google.cloud.storage.BlobInfo;
//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@SpringBootTest
class ShopmallProductApplicationTests {

	@Autowired
//	@Qualifier("brandService")
	BrandService brandService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	BrandDao brandDao;

	@Autowired
	CategoryDao categoryDao;

	@Autowired
	AttrService attrService;

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Autowired
	RedissonClient redissonClient;

	@Autowired
	AttrGroupDao attrGroupDao;

	@Autowired
	SkuSaleAttrValueDao skuSaleAttrValueDao;

	@Test
	void testRedisson(){
		System.out.println(redissonClient);
	}

	@Test
	void test(){
//		List<SpuItemAttrGroupVo> attrGroupWithAttrsBySpuId = attrGroupDao.getAttrGroupWithAttrsBySpuId(11L, 225L);
//		System.out.println(attrGroupWithAttrsBySpuId);

		List<SkuItemSaleAttrVo> saleAttrsBySpuId = skuSaleAttrValueDao.getSaleAttrsBySpuId(11L);
		System.out.println(saleAttrsBySpuId);
	}

	@Test
	void testStringRedisTemplate() {
		ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
		stringStringValueOperations.set("hello", "world_" + UUID.randomUUID().toString());
		String value = stringStringValueOperations.get("hello");
		System.out.println(value);
	}

	@Test
	void testError() {
		List<Long> attrIds = new ArrayList<Long>();
		List<AttrEntity> attrEntities = attrService.listByIds(attrIds);
		System.out.println(attrEntities);
	}

	@Test
	void findParentPathes() {
		Long[] catelogPath = categoryService.findCatelogPath(225L);
		log.info("完整路徑：{}", Arrays.asList(catelogPath));
	}

	@Test
	void testInsert() {
		BrandEntity brandEntity = new BrandEntity();
		brandEntity.setName("Apple");
		brandService.save(brandEntity);
//
//		BrandEntity brandEntity = new BrandEntity();
//		brandEntity.setBrandId(1L);
//		brandEntity.setDescript("Apple");
//		brandService.updateById(brandEntity);

//		List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1));
//		list.forEach((item) -> {
//			System.out.println(item);
//		});
	}

//	@Test
//	void contextLoads() {
//
//		BrandEntity brandEntity = new BrandEntity();
//		brandEntity.setName("Apple");
//		brandDao.insert(brandEntity);
//		brandEntity.setName("Ms");
//		brandDao.insert(brandEntity);
//
//		CategoryEntity categoryEntity = new CategoryEntity();
//		categoryEntity.setName("Test");
//		categoryDao.insert(categoryEntity);
////		brandService.save(brandEntity);
//		System.out.println("save successfully");
//	}

}
