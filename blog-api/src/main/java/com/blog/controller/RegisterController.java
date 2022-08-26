package com.blog.controller;

import com.blog.service.LoginService;
import com.blog.vo.Result;
import com.blog.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("register")
public class RegisterController {

    //将注册相关的功能放在LoginService里面
    @Autowired
    private LoginService loginService;

    //借用loginparams去接收参数
    @PostMapping
    public Result register (@RequestBody LoginParams loginParams){
        //sso 叫做单点登录。后期如果把登陆注册功能单独提出去（单独的服务，可以独立提供接口服务）
        return loginService.register(loginParams);
    }
}
