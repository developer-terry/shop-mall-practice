package com.practice.shopmall.member.dao;

import com.practice.shopmall.member.entity.MemberLoginLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员登录记录
 * 
 * @author TerryLee
 * @email p134030772@gmail.com
 * @date 2021-06-20 21:05:28
 */
@Mapper
public interface MemberLoginLogDao extends BaseMapper<MemberLoginLogEntity> {
	
}
