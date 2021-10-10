package com.practice.shopmall.order.dao;

import com.practice.shopmall.order.entity.RefundInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 退款信息
 * 
 * @author TerryLee
 * @email p134030772@gmail.com
 * @date 2021-06-20 22:13:55
 */
@Mapper
public interface RefundInfoDao extends BaseMapper<RefundInfoEntity> {
	
}
