package com.blog.controller;

import com.blog.service.SysUserService;
import com.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    //之前都是RequestBody意思是从前端的Body中进获取参数
    //此方法是主动从Header中获取Authorization变量，命名为token
    @GetMapping("currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token){

        //将token传入service
        return sysUserService.getUserInfoByToken(token);
    }
}