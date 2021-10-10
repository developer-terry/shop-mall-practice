package com.practice.shopmall.auth.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class UserRegisterVo {

    @NotEmpty(message = "用戶名不能為空")
    @Length(min = 6, max = 18, message = "用戶名必須是 6-18 位字元")
    private String userName;

    @NotEmpty(message = "密碼不能為空")
    @Length(min = 6, max = 18, message = "密碼必須是 6-18 位字元")
    private String password;

    @NotEmpty(message = "手機不能為空")
    @Pattern(regexp = "^09\\d{8}$", message = "手機格式不正確")
    private String phone;

//    @NotEmpty(message = "驗證碼不能為空")
    private String code;
}
