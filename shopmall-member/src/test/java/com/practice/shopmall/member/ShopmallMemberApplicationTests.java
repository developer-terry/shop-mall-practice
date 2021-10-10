package com.practice.shopmall.member;

import com.practice.shopmall.member.entity.MemberLevelEntity;
import com.practice.shopmall.member.service.MemberLevelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShopmallMemberApplicationTests {

	@Autowired
	MemberLevelService memberLevelService;

	@Test
	void contextLoads() {
		MemberLevelEntity memberLevelEntity = new MemberLevelEntity();
		memberLevelEntity.setName("高級會員");
		memberLevelService.save(memberLevelEntity);
	}

}
