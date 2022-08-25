package com.blog.service;

import com.blog.vo.Result;
import com.blog.vo.params.LoginParams;

public interface LoginService {

    //登陆验证
    Result login(LoginParams loginParams);

    //退出登陆
    Result logout(String token);


}
