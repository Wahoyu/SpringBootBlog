package com.blog.service;

import com.blog.entity.SysUser;
import com.blog.vo.Result;
import com.blog.vo.UserVo;

public interface SysUserService {

    //通过用户id查询用户名字
    SysUser findUserById(Long id);

    //通过用户名和密码查询用户
    SysUser findUser(String account, String password);

    //从Token中获取用户信息
    Result getUserInfoByToken(String token);

    //注册时判断用户是否存在
    SysUser findUserByAccount(String account);

    //注册时保存用户
    void save(SysUser sysUser);

    //通过userid找到找到UserVo
    UserVo findUserVoById(Long id);
}
