package com.practice.shopmall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.practice.common.utils.PageUtils;
import com.practice.shopmall.member.entity.MemberLoginLogEntity;

import java.util.Map;

/**
 * 会员登录记录
 *
 * @author TerryLee
 * @email p134030772@gmail.com
 * @date 2021-06-20 21:05:28
 */
public interface MemberLoginLogService extends IService<MemberLoginLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

