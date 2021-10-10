package com.practice.shopmall.auth.controller;

import com.alibaba.fastjson.TypeReference;
import com.practice.common.constant.AuthServiceConstant;
import com.practice.common.utils.R;
import com.practice.common.vo.MemberResponseVo;
import com.practice.shopmall.auth.feign.MemberFeignService;
import com.practice.shopmall.auth.vo.UserLoginVo;
import com.practice.shopmall.auth.vo.UserRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

//    @GetMapping("/login.html")
//    public String loginPage() {
//        return "login";
//    }
//
//    @GetMapping("/reg.html")
//    public String registerPage() {
//        return "reg";
//    }

    @Autowired
    MemberFeignService memberFeignService;

    @PostMapping("register")
    public String register(@Valid UserRegisterVo userRegisterVo, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
//            Map<String, String> errors = bindingResult.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            bindingResult.getFieldErrors().forEach(item->{
                errors.put(item.getField(), item.getDefaultMessage());
            });
//            model.addAttribute("errors", errors);
            redirectAttributes.addFlashAttribute("errors", errors);
//            return "forword:/reg.html";
//            return "reg";
            return "redirect:http://auth.shopmall-test.com/reg.html";
        }

        //註冊
        //TODO 校驗驗證碼是否正確

        R r = memberFeignService.register(userRegisterVo);
        if(r.getCode() == 0) {

            return "redirect:http://auth.shopmall-test.com/login.html";
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("msg", r.getData("msg", new TypeReference<String>() {}));
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.shopmall-test.com/reg.html";
        }

//        return "redirect:/login.html";
    }

    @GetMapping("login.html")
    public String loginPage(HttpSession httpSession){
        Object attribute = httpSession.getAttribute(AuthServiceConstant.LOGIN_USER);
        if(attribute == null) {
            return "login";
        } else {
            return "redirect:http://shopmall-test.com";
        }
    }

    @PostMapping("/login")
    public String login(UserLoginVo userLoginVo, RedirectAttributes redirectAttributes, HttpSession httpSession) {
        R r = memberFeignService.login(userLoginVo);
        if(r.getCode() == 0) {
            MemberResponseVo memberResponseVo = r.getData("data", new TypeReference<MemberResponseVo>(){});
            httpSession.setAttribute(AuthServiceConstant.LOGIN_USER, memberResponseVo);
            return "redirect:http://shopmall-test.com";
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("msg", r.getData("msg", new TypeReference<String>(){}));
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.shopmall-test.com/login.html";
        }
    }
}
