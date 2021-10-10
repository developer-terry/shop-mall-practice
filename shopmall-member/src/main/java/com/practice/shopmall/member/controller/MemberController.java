package com.practice.shopmall.member.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.practice.shopmall.member.exception.MobileExistException;
import com.practice.shopmall.member.exception.UsernameExistException;
import com.practice.shopmall.member.feign.CouponFeignService;
import com.practice.shopmall.member.vo.SocialUser;
import com.practice.shopmall.member.vo.UserLoginVo;
import com.practice.shopmall.member.vo.UserRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.practice.shopmall.member.entity.MemberEntity;
import com.practice.shopmall.member.service.MemberService;
import com.practice.common.utils.PageUtils;
import com.practice.common.utils.R;



/**
 * 会员
 *
 * @author TerryLee
 * @email p134030772@gmail.com
 * @date 2021-06-20 21:05:28
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private CouponFeignService couponFeignService;

    @RequestMapping("/coupons")
    public R test() {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("Terry");

        R memberCoupons = couponFeignService.memberCoupons();
        return R.ok().put("memebr", memberEntity).put("coupons", memberCoupons.get("coupons"));
    }

    @PostMapping("/oauth2/login")
    public R oauthLogin(@RequestBody SocialUser socialUser) {
        MemberEntity entity = memberService.login(socialUser);
        if(entity != null) {
            return R.ok().setData(entity);
        } else {
            return R.error();
        }
    }

    @PostMapping("/login")
    public R login(@RequestBody UserLoginVo userLoginVo) {
        MemberEntity memberEntity = memberService.login(userLoginVo);
        if(memberEntity != null) {
            return R.ok().setData(memberEntity);
        } else {
            return R.error("帳號或密碼錯誤");
        }
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }

    //TODO 異常統整
    @PostMapping("register")
    public R register(@RequestBody UserRegisterVo userRegisterVo) {
        try {
            memberService.register(userRegisterVo);
        } catch (MobileExistException mobileExistException) {
            return R.error(mobileExistException.getMessage());
        } catch (UsernameExistException usernameExistException) {
            return R.error(usernameExistException.getMessage());
        }

        return R.ok();
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
