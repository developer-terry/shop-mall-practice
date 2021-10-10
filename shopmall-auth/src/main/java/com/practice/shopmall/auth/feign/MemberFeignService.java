package com.practice.shopmall.auth.feign;

import com.practice.common.utils.R;
import com.practice.shopmall.auth.bo.CustomOAuth2User;
import com.practice.shopmall.auth.vo.SocialUser;
import com.practice.shopmall.auth.vo.UserLoginVo;
import com.practice.shopmall.auth.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("shopmall-member")
public interface MemberFeignService {

    @PostMapping("/member/member/register")
    public R register(@RequestBody UserRegisterVo userRegisterVo);

    @PostMapping("/member/member/login")
    public R login(@RequestBody UserLoginVo userLoginVo);

    @PostMapping("/member/member/oauth2/login")
    public R oauthLogin(@RequestBody SocialUser socialUser);
}
