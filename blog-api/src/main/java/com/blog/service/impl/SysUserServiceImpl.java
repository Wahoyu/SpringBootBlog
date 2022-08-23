package com.blog.service.impl;

import com.blog.entity.SysUser;
import com.blog.mapper.SysUserMapper;
import com.blog.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    //通过用户id查询用户名字
    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if(sysUser == null){
            sysUser = new SysUser();
            sysUser.setNickname("Wahoyu");
        }
        return sysUser;
    }
}
