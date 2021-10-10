package com.practice.shopmall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.practice.common.utils.PageUtils;
import com.practice.shopmall.member.entity.MemberCollectSubjectEntity;

import java.util.Map;

/**
 * 会员收藏的专题活动
 *
 * @author TerryLee
 * @email p134030772@gmail.com
 * @date 2021-06-20 21:05:28
 */
public interface MemberCollectSubjectService extends IService<MemberCollectSubjectEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

