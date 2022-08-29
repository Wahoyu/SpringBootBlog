package com.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.blog.entity.SysUser;
import com.blog.mapper.SysUserMapper;
import com.blog.service.LoginService;
import com.blog.service.SysUserService;
import com.blog.utils.JWTUtils;
import com.blog.vo.ErrorCode;
import com.blog.vo.LoginUserVo;
import com.blog.vo.Result;
import com.blog.vo.UserVo;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private LoginService loginService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

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

    //通过用户名和密码对用户进行查询
    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.eq(SysUser::getPassword, password);
        queryWrapper.select(SysUser::getAccount, SysUser::getId, SysUser::getAvatar, SysUser::getNickname);
        //只查找一个，不继续往下找，节省sql效率
        queryWrapper.last("limit 1");

        return sysUserMapper.selectOne(queryWrapper);
    }

    //通过Token获取用户信息
    @Override
    public Result getUserInfoByToken(String token) {

        //检查token并生成sysUser
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            Result.fail(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
        }

        //创建一个适配前端的 用户信息 对象loginUserVo
        LoginUserVo loginUserVo = new LoginUserVo();
        //将上述sysUser对象的信息全部复制到Vo对象中
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setAvatar(sysUser.getAvatar());
        loginUserVo.setId(sysUser.getId());
        loginUserVo.setNickname(sysUser.getNickname());

        //返回用户信息对象Vo
        return Result.success(loginUserVo);
    }

    //注册时根据账户查找用户
    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();

        //注意：此处的查询wrapper仔细考虑。我们使用SysUserh类中的getAccount方法，让他等于我们前端传入的account数值进行查询
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.last("limit 1");
        return this.sysUserMapper.selectOne(queryWrapper);
    }

    //注册时保存用户
    @Override
    public void save(SysUser sysUser) {

        //id会自动生成
        //这个地方默认生成的id时分布式id，采用的是雪花算法
        //在此处我们就不对用户id进行配置了（在SysUser的id上添加注解）-> @TableIdId(type = IdType.AUTO)
        //以后 用户多了之后 要进行分表操作 id就需要使用分布式id
        this.sysUserMapper.insert(sysUser);
    }

    //通过作者id找到UserVo
    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if(sysUser == null){
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("Wahoyu");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        return userVo;
    }
}
