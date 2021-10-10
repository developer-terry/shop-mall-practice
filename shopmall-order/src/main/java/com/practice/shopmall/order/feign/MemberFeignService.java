package com.practice.shopmall.order.feign;

import com.practice.shopmall.order.vo.MemberRecieveAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("shopmall-member")
public interface MemberFeignService {

    @GetMapping("/member/memberreceiveaddress/{memberId}/addresses")
    public List<MemberRecieveAddressVo> getAddress(@PathVariable("memberId") Long memberId);
}
