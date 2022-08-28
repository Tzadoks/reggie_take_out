package com.tzadok.common;

/**
 * @author Tzadok
 * @date 2022/8/24 17:05:48
 * @project reggie_take_out
 * @description: 自定义异常
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
