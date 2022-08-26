package com.blog.service;

import com.blog.vo.Result;
import com.blog.vo.params.LoginParams;
import org.springframework.transaction.annotation.Transactional;

//事务注解
@Transactional
public interface LoginService {

    //登陆验证
    Result login(LoginParams loginParams);

    //退出登陆
    Result logout(String token);

    //注册
    Result register(LoginParams loginParams);
}
