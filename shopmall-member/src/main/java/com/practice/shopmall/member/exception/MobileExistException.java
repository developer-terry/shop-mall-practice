package com.practice.shopmall.member.exception;

public class MobileExistException extends RuntimeException{
    public MobileExistException() {
        super("手機已被使用");
    }
}
