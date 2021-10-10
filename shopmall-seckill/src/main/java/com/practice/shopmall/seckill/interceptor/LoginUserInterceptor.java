package com.practice.shopmall.seckill.interceptor;

import com.practice.common.constant.AuthServiceConstant;
import com.practice.common.vo.MemberResponseVo;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginUserInterceptor implements HandlerInterceptor {

    public static ThreadLocal<MemberResponseVo> loginUser = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // /order/order/status/123456
        boolean isMatch = new AntPathMatcher().match("/kill", request.getRequestURI());
        if(isMatch) {
            MemberResponseVo attribute = (MemberResponseVo) request.getSession().getAttribute(AuthServiceConstant.LOGIN_USER);

            if(attribute != null) {
                loginUser.set(attribute);
                return true;
            } else {
                request.getSession().setAttribute("msg", "請先登入會員");
                response.sendRedirect("http://auth.shopmall-test.com/login.html");
                return false;
            }
        }

        return true;
    }
}
