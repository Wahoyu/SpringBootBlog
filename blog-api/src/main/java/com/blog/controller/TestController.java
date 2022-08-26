package com.blog.controller;

import com.blog.entity.SysUser;
import com.blog.utils.UserThreadLocal;
import com.blog.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping
    public Result test(){
        SysUser sysUser = UserThreadLocal.get();
        System.out.println();
        return Result.success(null);
    }
}
