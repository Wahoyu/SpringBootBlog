package com.blog.service;

import com.blog.entity.SysUser;
import com.blog.vo.Result;
import com.blog.vo.params.LoginParams;
import org.springframework.transaction.annotation.Transactional;

//事务注解
@Transactional
public interface LoginService {

    //登陆验证
    Result login(LoginParams loginParams);

    //判断token是否为空，判断token是否合法，判断redis中有没有token
    SysUser checkToken(String token);

    //退出登陆
    Result logout(String token);

    //注册
    Result register(LoginParams loginParams);
}
