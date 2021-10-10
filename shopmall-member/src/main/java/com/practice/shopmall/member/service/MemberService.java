package com.practice.shopmall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.practice.common.utils.PageUtils;
import com.practice.shopmall.member.entity.MemberEntity;
import com.practice.shopmall.member.exception.MobileExistException;
import com.practice.shopmall.member.exception.UsernameExistException;
import com.practice.shopmall.member.vo.SocialUser;
import com.practice.shopmall.member.vo.UserLoginVo;
import com.practice.shopmall.member.vo.UserRegisterVo;

import java.util.Map;

/**
 * 会员
 *
 * @author TerryLee
 * @email p134030772@gmail.com
 * @date 2021-06-20 21:05:28
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void register(UserRegisterVo userRegisterVo);

    void checkMobileUnique(String mobile) throws MobileExistException;

    void checkUsernameUnique(String username) throws UsernameExistException;

    MemberEntity login(UserLoginVo userLoginVo);

    MemberEntity login(SocialUser socialUser);
}

