package com.blog.controller;

import com.blog.entity.SysUser;
import com.blog.mapper.SysUserMapper;
import com.blog.service.LoginService;
import com.blog.service.SysUserService;
import com.blog.vo.Result;
import com.blog.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class LoginController {

    /*
    //不推荐使用SysUserService，应该让UserService只负责与User表操作相关的操作
    @Autowired
    SysUserService sysUserService;
    */

    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result login(@RequestBody LoginParams loginParams){
        //登陆 验证用户 访问用户表
        return loginService.login(loginParams);
    }
}
