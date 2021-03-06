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
        //TODO ??????????????????????????????????????????????????????
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
     * ??????????????????????????????
     * ??????????????????????????????
     *     @Caching(evict = {
     *             @CacheEvict(value = {"category"}, key = "'firstLevelCategories'"),
     *             @CacheEvict(value = {"category"}, key = "'categories'")
     *     })
     *
     *     @CacheEvict(value = {"category"}, allEntries = true)
     *
     * ?????????????????????????????? ????????????????????????????????? ?????????????????????????????????
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

    //?????????????????????????????? ??????????????????????????????????????????????????? ??????????????? ????????????????????????
    //????????????????????????????????? ?????????????????? ?????????????????? ????????????????????? ???????????????????????????????????????????????????
    //????????????
    //  ?????????????????? ??????????????????
    //  key ?????????????????? ???????????????: SimpleKey[] (???????????? key ???)
    //  ????????? value ?????? ???????????? jdk ??????????????? ?????????????????????????????? redis
    //  ????????????????????? -1
    //????????????
    //  ??????????????? key???key ???????????? ?????? SpEl
    //  ???????????????????????????spring.cache.redis.time-to-live=3600000(ms)
    //  ?????????????????? json ?????????????????? RedisCacheConfiguration ??????
    //Spring-Cache ?????????
    //  ????????????
    //    ??????????????????????????? null ???????????????????????? null ??????
    //    ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????sync = true
    //    ???????????????????????? key ?????????????????????????????????????????? ??????????????????
    //  ????????????
    //    ????????????
    //    ?????? Canal ????????? MySQL ???????????????????????????
    //    ???????????? ??????????????????????????????
    //  ?????????
    //    ????????????(???????????? ????????? ??????????????????????????????) ????????? Spring-Cache????????????(????????????????????????????????????????????????)
    //    ???????????????????????????
    //    ???????????? ??????????????????????????????
    //  ?????????CacheManager(RedisCacheManager) -> Cache(RedisCache) -> Cache ??????????????????


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

    //TODO ???????????????????????? ERROR

    /**
     * spring boot 2.0 ?????????????????? lettuce ???????????? redis ???????????? ????????? netty ??????????????????
     * lettuce ??? bug ?????? netty ?????????????????? -Xmx300m netty ?????????????????????????????? ???????????? -Xmx300m
     * ???????????? -Dio.netty.maxDirectMemory ????????????
     * @return
     */
    public Map<String, List<CatelogSecondVo>> getCatalogJson2() {
        /**
         * ??????????????? ??????????????????
         * ??????????????????(????????????) ??????????????????
         * ?????? ??????????????????
         */
        String catalogJson = stringRedisTemplate.opsForValue().get("catalogJson");

        Map<String, List<CatelogSecondVo>> catalogJsonMap;

        if(StringUtils.isEmpty(catalogJson)) {
            catalogJsonMap = getCatalogJsonFromDB();
            //??????????????? JSON ???????????????
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
        //?????????????????? ??????????????????????????? ?????????
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

        //??????????????? ??? redis ??????
        String token = UUID.randomUUID().toString();
        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent("lock", token, 30, TimeUnit.SECONDS);
        if(success) { //???????????? ????????????
            //?????????????????? ??????????????????????????? ?????????
//            stringRedisTemplate.expire("lock", 30, TimeUnit.SECONDS);
            Map<String, List<CatelogSecondVo>> catalogJsonFromDB;
            try {
                catalogJsonFromDB = getCatalogJsonFromDB();
            } finally {
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), token);
            }
//            stringRedisTemplate.delete("lock");
//            ??????????????? ?????????????????? ???????????????????????????
//            String currentLockValue = stringRedisTemplate.opsForValue().get("lock");
//            if(token.equals(currentLockValue)) {
//                //??????????????????
//                stringRedisTemplate.delete("lock");
//            }

//            Lua ????????????
            return catalogJsonFromDB;
        } else { //???????????? ??????
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