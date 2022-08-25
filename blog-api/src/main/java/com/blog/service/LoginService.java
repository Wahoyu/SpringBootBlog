package com.blog.service;

import com.blog.vo.Result;
import com.blog.vo.params.LoginParams;

public interface LoginService {

    //登陆验证
    Result login(LoginParams loginParams);
}
