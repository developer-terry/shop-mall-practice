package com.practice.shopmall.auth.config;

import com.alibaba.fastjson.TypeReference;
import com.practice.common.utils.R;
import com.practice.common.vo.MemberResponseVo;
import com.practice.shopmall.auth.bo.CustomOAuth2User;
import com.practice.shopmall.auth.feign.MemberFeignService;
import com.practice.shopmall.auth.service.CustomOAuth2UserService;
import com.practice.shopmall.auth.vo.SocialUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomOAuth2UserService oauthUserService;

    @Autowired
    MemberFeignService memberFeignService;

    @Autowired
    HttpSession httpSession;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/login.html").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .loginPage("/login.html")
                .userInfoEndpoint()
                .userService(oauthUserService)
                .and()
                .successHandler(new AuthenticationSuccessHandler() {

                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                        Authentication authentication) throws IOException, ServletException {

                        System.out.println(authentication.getPrincipal());
                        System.out.println(authentication.getPrincipal().getClass());

                        DefaultOidcUser oauthUser = (DefaultOidcUser) authentication.getPrincipal();
//                        userService.processOAuthPostLogin(oauthUser.getEmail());

                        SocialUser socialUser = new SocialUser();
                        socialUser.setEmail(oauthUser.getEmail());
                        socialUser.setName(oauthUser.getName());
                        R r = memberFeignService.oauthLogin(socialUser);
                        if(r.getCode() == 0) {
                            MemberResponseVo memberResponseVo = r.getData(new TypeReference<MemberResponseVo>(){});
                            System.out.println("memberResponseVo----------");
                            System.out.println(memberResponseVo);
                            httpSession.setAttribute("loginUser", memberResponseVo);
                            response.sendRedirect("http://shopmall-test.com");
                        } else {
                            response.sendRedirect("/");
                        }
                    }
                });
//                .defaultSuccessUrl("http://shopmall-test.com")
//                .and()
//                .logout()
//                .logoutSuccessUrl("/login.html");
    }
}