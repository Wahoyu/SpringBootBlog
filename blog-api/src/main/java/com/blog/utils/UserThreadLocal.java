package com.blog.utils;

import com.blog.entity.SysUser;

public class UserThreadLocal {

    private UserThreadLocal(){}

    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    //存入数据
    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }

    //获取数据
    public static SysUser get(){
        return LOCAL.get();
    }

    //删除数据
    public static void remove(){
        LOCAL.remove();
    }
}