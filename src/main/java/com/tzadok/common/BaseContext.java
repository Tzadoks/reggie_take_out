package com.tzadok.common;

/**
 * @author Tzadok
 * @date 2022/8/24 15:51:36
 * @project reggie_take_out
 * @description: 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setThreadLocal(Long id ){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }

}
