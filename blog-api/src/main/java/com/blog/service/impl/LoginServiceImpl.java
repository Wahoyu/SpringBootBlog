package com.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.blog.entity.SysUser;
import com.blog.service.LoginService;
import com.blog.service.SysUserService;
import com.blog.utils.JWTUtils;
import com.blog.vo.ErrorCode;
import com.blog.vo.Result;
import com.blog.vo.params.LoginParams;
import org.apache.commons.codec.cli.Digest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.patterns.IToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    //加密盐
    private static final String salt = "wahoyu!@#";

    //登陆验证
    @Override
    public Result login(LoginParams loginParams) {

        //获取前端的用户名和密码参数
        String account = loginParams.getAccount();
        String password = loginParams.getPassword();

        //判断参数们进行空值判断
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }

        //对密码进行md5加密   (需要导入依赖)(注意是commons-codec类)
        password = DigestUtils.md5Hex(password + salt);

        //去uesr表中查询加密后的用户名和密码是否存在
        SysUser sysUser = sysUserService.findUser(account,password);

        //判断表中是否存在用户名
        if(sysUser == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }

        //生成token
        String token = JWTUtils.createToken(sysUser.getId());

        //将Token放入Redis并设置时间（1天） key:token  value:sysUser的JSON形式 1天就过期
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);

        //将token信息return给前端
        return Result.success(token);
    }

    //退出登陆
    @Override
    public Result logout(String token) {
        //从Redis中将Token的信息进行删除
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }


}
