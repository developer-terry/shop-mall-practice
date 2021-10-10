package com.practice.shopmall.member.exception;

public class UsernameExistException extends RuntimeException{
    public UsernameExistException() {
        super("用戶名已被使用");
    }
}
