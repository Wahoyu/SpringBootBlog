package com.blog.service;

import com.blog.entity.SysUser;

public interface SysUserService {

    //通过用户id查询用户名字
    SysUser findUserById(Long id);
}
