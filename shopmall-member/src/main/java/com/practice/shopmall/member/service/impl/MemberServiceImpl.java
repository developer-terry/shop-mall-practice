package com.practice.shopmall.member.service.impl;

import com.practice.shopmall.member.dao.MemberLevelDao;
import com.practice.shopmall.member.entity.MemberLevelEntity;
import com.practice.shopmall.member.exception.MobileExistException;
import com.practice.shopmall.member.exception.UsernameExistException;
import com.practice.shopmall.member.vo.SocialUser;
import com.practice.shopmall.member.vo.UserLoginVo;
import com.practice.shopmall.member.vo.UserRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.practice.common.utils.PageUtils;
import com.practice.common.utils.Query;

import com.practice.shopmall.member.dao.MemberDao;
import com.practice.shopmall.member.entity.MemberEntity;
import com.practice.shopmall.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    MemberLevelDao memberLevelDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void register(UserRegisterVo userRegisterVo) {
        MemberEntity memberEntity = new MemberEntity();
        //設置默認等級
        MemberLevelEntity memberLevelEntity = memberLevelDao.getDefaultLevel();
        memberEntity.setLevelId(memberLevelEntity.getId());

        //檢查用戶名和手機號是否唯一
        checkMobileUnique(userRegisterVo.getPhone());
        checkUsernameUnique(userRegisterVo.getUserName());

        memberEntity.setMobile(userRegisterVo.getPhone());
        memberEntity.setUsername(userRegisterVo.getUserName());

        //密碼
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(userRegisterVo.getPassword());
        memberEntity.setPassword(encode);

        //其他的默認訊息

        //儲存
        this.baseMapper.insert(memberEntity);
    }

    @Override
    public void checkMobileUnique(String mobile) throws MobileExistException{
        int count = baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", mobile));
        if(count > 0) {
            throw new MobileExistException();
        }
    }

    @Override
    public void checkUsernameUnique(String username) throws UsernameExistException{
        int count = baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("username", username));

        if(count > 0) {
            throw new UsernameExistException();
        }
    }

    @Override
    public MemberEntity login(UserLoginVo userLoginVo) {
        String loginAccount = userLoginVo.getLoginAccount();
        String password = userLoginVo.getPassword();

        MemberEntity memberEntity = baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("username", loginAccount).or().eq("mobile", loginAccount));

        if(memberEntity == null) {
            return null;
        } else {
            String dbPassword = memberEntity.getPassword();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean matches = encoder.matches(password, dbPassword);

            if(matches) {
                return memberEntity;
            } else {
                return null;
            }
        }
    }

    @Override
    public MemberEntity login(SocialUser socialUser) {
        //登入+註冊
        String email = socialUser.getEmail();
        MemberEntity memberEntity = baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("username", email));

        if(memberEntity == null) {
            memberEntity = new MemberEntity();
            memberEntity.setUsername(email);
            memberEntity.setNickname(socialUser.getName());
            baseMapper.insert(memberEntity);
        }

        return memberEntity;
    }

}