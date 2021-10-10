package com.practice.shopmall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.practice.shopmall.product.service.CategoryBrandRelationService;
import com.practice.shopmall.product.vo.CatelogSecondVo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.practice.common.utils.PageUtils;
import com.practice.common.utils.Query;

import com.practice.shopmall.product.dao.CategoryDao;
import com.practice.shopmall.product.entity.CategoryEntity;
import com.practice.shopmall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redisson;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    public List<CategoryEntity> listWithTree() {

        List<CategoryEntity> entityList = this.baseMapper.selectList(null);

        List<CategoryEntity> level1Menus = entityList.stream()
                .filter(entity -> entity.getCatLevel() == 1)
                .map(entity -> {
                    entity.setChildren(getChildren(entity, entityList));
                    return entity;
                })
                .sorted((current, next) -> next.getSort() - current.getSort())
                .collect(Collectors.toList());

        return level1Menus;
    }

    @Override
    public void removeCategoryByIds(List<Long> asList) {
        //TODO 檢查當前刪除的分類是否被其他地方引用
        baseMapper.deleteBatchIds(asList);
    }

    protected List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {

        List<CategoryEntity> children = all.stream()
                .filter(entity -> entity.getParentCid() == root.getCatId())
                .map(entity -> {
                    entity.setChildren(getChildren(entity, all));
                    return entity;
                })
                .sorted((current, next) -> next.getSort() - current.getSort())
                .collect(Collectors.toList());

        return children;
    }

    public Long[] findCatelogPath(Long catelogId) {
        List<Long> pathes = new ArrayList<>();
        List<Long> parentPath = this.findParentPath(catelogId, pathes);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 集聯更新所有關聯數據
     * 同時進行多種緩存操作
     *     @Caching(evict = {
     *             @CacheEvict(value = {"category"}, key = "'firstLevelCategories'"),
     *             @CacheEvict(value = {"category"}, key = "'categories'")
     *     })
     *
     *     @CacheEvict(value = {"category"}, allEntries = true)
     *
     * 儲存同一種類型的數據 都可以指定為同一個分區 分區名稱默認為緩存前綴
     * @param category
     */
//    @CacheEvict(value = {"category"}, key = "'firstLevelCategories'")
//    @Caching(evict = {
//            @CacheEvict(value = {"category"}, key = "'firstLevelCategories'"),
//            @CacheEvict(value = {"category"}, key = "'categories'")
//    })
    @CacheEvict(value = {"category"}, allEntries = true)
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
    }

    //每一個需要緩存的數據 我們都需要指定要放到哪個名字的緩存 緩存的分區 依照業務類型區分
    //當前方法的結果需要緩存 如果緩存中有 方法不用調用 如果緩存中沒有 則調用方法最後將方法的結果放入緩存
    //默認行為
    //  如果緩存中有 方法不用調用
    //  key 默認自動生成 緩存的名字: SimpleKey[] (自動生成 key 值)
    //  緩存的 value 的值 默認使用 jdk 序列化機制 將序列化後的數據存到 redis
    //  默認過期時間是 -1
    //自訂操作
    //  生成緩存的 key：key 屬性指定 接受 SpEl
    //  指定緩存過期時間：spring.cache.redis.time-to-live=3600000(ms)
    //  將數據保存為 json 格式：自定義 RedisCacheConfiguration 即可
    //Spring-Cache 的不足
    //  讀模式：
    //    緩存穿透：查詢一個 null 數據，解決：緩存 null 數據
    //    緩存擊穿：大量併發進來同時查詢一個正好過期的數據，解決：加上鎖，默認不加鎖，sync = true
    //    緩存雪崩：大量的 key 同時過期，解決：加上隨機時間 加上過期時間
    //  寫模式：
    //    讀寫加鎖
    //    引入 Canal 感知到 MySQL 的更新去更新資料庫
    //    讀多寫多 直接去資料庫查詢就好
    //  總結：
    //    常規數據(讀多寫少 即時性 一致性要求不高的資料) 可以用 Spring-Cache，寫模式(只要緩存的數據有過期時間就足夠了)
    //    特殊數據：特殊設計
    //    讀多寫多 直接去資料庫查詢就好
    //  原理：CacheManager(RedisCacheManager) -> Cache(RedisCache) -> Cache 負責緩存讀寫


    @Cacheable(value = {"category"}, key = "'firstLevelCategories'", sync = true)
    @Override
    public List<CategoryEntity> getFirstLevelCategories() {
        System.out.println("getFirstLevelCategories...");
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("cat_level", 1));
        return categoryEntities;
    }

    @Cacheable(value = {"category"}, key = "'categories'")
    @Override
    public Map<String, List<CatelogSecondVo>> getCatalogJson() {
        List<CategoryEntity> selectList = baseMapper.selectList(null);
        List<CategoryEntity> firstLevelCategories = getParent_cid(selectList, 0L);

        Map<String, List<CatelogSecondVo>> catalogJson = firstLevelCategories.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            List<CategoryEntity> level2CategoryEntities = getParent_cid(selectList, v.getCatId());
            List<CatelogSecondVo> catelogSecondVoList = null;
            if(level2CategoryEntities != null) {
                catelogSecondVoList = level2CategoryEntities.stream().map(level2CategoryEntity -> {
                    CatelogSecondVo catelogSecondVo = new CatelogSecondVo(v.getCatId().toString(), null, level2CategoryEntity.getCatId().toString(), level2CategoryEntity.getName());
                    List<CategoryEntity> level3CategoryEntities = getParent_cid(selectList, level2CategoryEntity.getCatId());//baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", level2CategoryEntity.getCatId()));
                    if(level3CategoryEntities != null) {
                        List<CatelogSecondVo.CatelogThirdVo> catelogThirdVoList = level3CategoryEntities.stream().map(level3CategoryEntity -> {
                            CatelogSecondVo.CatelogThirdVo catelogThirdVo = new CatelogSecondVo.CatelogThirdVo(level2CategoryEntity.getCatId().toString(), level3CategoryEntity.getCatId().toString(), level3CategoryEntity.getName());
                            return catelogThirdVo;
                        }).collect(Collectors.toList());

                        catelogSecondVo.setCatalog3List(catelogThirdVoList);
                    }

                    return catelogSecondVo;
                }).collect(Collectors.toList());
            }

            return catelogSecondVoList;
        }));

        return catalogJson;
    }

    //TODO 產生堆外內存溢出 ERROR

    /**
     * spring boot 2.0 以後默認使用 lettuce 作為操作 redis 的客戶端 他使用 netty 進行網路通信
     * lettuce 的 bug 導致 netty 堆外內存溢出 -Xmx300m netty 如果沒有指定堆外內存 默認使用 -Xmx300m
     * 可以通過 -Dio.netty.maxDirectMemory 進行設置
     * @return
     */
    public Map<String, List<CatelogSecondVo>> getCatalogJson2() {
        /**
         * 空結果緩存 解決緩存穿透
         * 設置過期時間(加隨機值) 解決緩存雪崩
         * 加鎖 解決緩存擊穿
         */
        String catalogJson = stringRedisTemplate.opsForValue().get("catalogJson");

        Map<String, List<CatelogSecondVo>> catalogJsonMap;

        if(StringUtils.isEmpty(catalogJson)) {
            catalogJsonMap = getCatalogJsonFromDB();
            //將對象轉為 JSON 放入緩存中
//            String jsonString = JSON.toJSONString(catalogJsonMap);
//            stringRedisTemplate.opsForValue().set("catalogJson", jsonString, 1, TimeUnit.DAYS);
            return catalogJsonMap;
        }

        catalogJsonMap = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<CatelogSecondVo>>>() {});
        return catalogJsonMap;
    }

    public Map<String, List<CatelogSecondVo>> getCatalogJsonFromDBWithRedissonLock() {

        RLock rLock = redisson.getLock("catalogJsonLock");
        rLock.lock();
        //設置過期時間 必須和加鎖是同步的 原子的
//            stringRedisTemplate.expire("lock", 30, TimeUnit.SECONDS);
        Map<String, List<CatelogSecondVo>> catalogJsonFromDB;
        try {
            catalogJsonFromDB = getCatalogJsonFromDB();
        } finally {
            rLock.unlock();
        }

        return catalogJsonFromDB;
    }

    public Map<String, List<CatelogSecondVo>> getCatalogJsonFromDBWithRedisLock() {

        //佔分佈式鎖 去 redis 佔坑
        String token = UUID.randomUUID().toString();
        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent("lock", token, 30, TimeUnit.SECONDS);
        if(success) { //加鎖成功 執行業務
            //設置過期時間 必須和加鎖是同步的 原子的
//            stringRedisTemplate.expire("lock", 30, TimeUnit.SECONDS);
            Map<String, List<CatelogSecondVo>> catalogJsonFromDB;
            try {
                catalogJsonFromDB = getCatalogJsonFromDB();
            } finally {
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), token);
            }
//            stringRedisTemplate.delete("lock");
//            獲取值對比 對比成功刪除 必須也要是原子操作
//            String currentLockValue = stringRedisTemplate.opsForValue().get("lock");
//            if(token.equals(currentLockValue)) {
//                //刪除自己的鎖
//                stringRedisTemplate.delete("lock");
//            }

//            Lua 腳本解鎖
            return catalogJsonFromDB;
        } else { //加鎖失敗 重試
            try {
                Thread.sleep(2000);
            } catch (Exception e) {

            }
            return getCatalogJsonFromDBWithRedisLock();
        }
    }

    private Map<String, List<CatelogSecondVo>> getCatalogJsonFromDB() {
        String catalogJsonString = stringRedisTemplate.opsForValue().get("catalogJson");

        if(!StringUtils.isEmpty(catalogJsonString)) {
            Map<String, List<CatelogSecondVo>> catalogJsonMap = JSON.parseObject(catalogJsonString, new TypeReference<Map<String, List<CatelogSecondVo>>>() {});
            return catalogJsonMap;
        }

        List<CategoryEntity> selectList = baseMapper.selectList(null);

        List<CategoryEntity> firstLevelCategories = getParent_cid(selectList, 0L);
        Map<String, List<CatelogSecondVo>> catalogJson = firstLevelCategories.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            List<CategoryEntity> level2CategoryEntities = getParent_cid(selectList, v.getCatId());
            List<CatelogSecondVo> catelogSecondVoList = null;
            if(level2CategoryEntities != null) {
                catelogSecondVoList = level2CategoryEntities.stream().map(level2CategoryEntity -> {
                    CatelogSecondVo catelogSecondVo = new CatelogSecondVo(v.getCatId().toString(), null, level2CategoryEntity.getCatId().toString(), level2CategoryEntity.getName());
                    List<CategoryEntity> level3CategoryEntities = getParent_cid(selectList, level2CategoryEntity.getCatId());//baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", level2CategoryEntity.getCatId()));
                    if(level3CategoryEntities != null) {
                        List<CatelogSecondVo.CatelogThirdVo> catelogThirdVoList = level3CategoryEntities.stream().map(level3CategoryEntity -> {
                            CatelogSecondVo.CatelogThirdVo catelogThirdVo = new CatelogSecondVo.CatelogThirdVo(level2CategoryEntity.getCatId().toString(), level3CategoryEntity.getCatId().toString(), level3CategoryEntity.getName());
                            return catelogThirdVo;
                        }).collect(Collectors.toList());

                        catelogSecondVo.setCatalog3List(catelogThirdVoList);
                    }

                    return catelogSecondVo;
                }).collect(Collectors.toList());
            }

            return catelogSecondVoList;
        }));

        return catalogJson;
    }


    public Map<String, List<CatelogSecondVo>> getCatalogJsonFromDBWithLocalLock() {

        synchronized (this) {
            return getCatalogJsonFromDB();
        }
    }

    private List<CategoryEntity> getParent_cid(List<CategoryEntity> categoryEntityList, Long parentCid) {
        List<CategoryEntity> selectList = categoryEntityList.stream().filter(categoryEntity -> categoryEntity.getParentCid() == parentCid).collect(Collectors.toList());

        return selectList;
//        return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
    }

    public List<Long> findParentPath(Long catelogId, List<Long> pathes) {

        CategoryEntity categoryEntity = this.getById(catelogId);
        if(categoryEntity == null) {
            return pathes;
        }

        pathes.add(catelogId);

        if(categoryEntity.getParentCid() != 0) {
            this.findParentPath(categoryEntity.getParentCid(), pathes);
        }

        return pathes;
    }
}