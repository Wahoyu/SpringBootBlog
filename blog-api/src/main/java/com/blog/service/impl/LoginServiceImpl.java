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

import java.util.Map;
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

        //去user表中查询加密后的用户名和密码是否存在
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

    //获取用户信息和设置拦截器的时候checkToken
    //通过Token获取用户信息
    @Override
    public SysUser checkToken(String token) {

        //判断token是否为空值
        if (StringUtils.isBlank(token)){
            return null;
        }
        //1.通过JWT对token进行合法性校验，不合格则报错
        Map<String, Object> map = JWTUtils.checkToken(token);
        if (map == null){
            return null;
        }

        //我们当初进行Redis存储时，key是token，value是JSON.toJSONString(sysUser)
        //通过JWT格式检查之后，在Redis中进行查出token对应的sysuser的json值，没有则报错
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userJson)){
            return null;
        }

        //通过json解析器，将userJson信息转化成SysUser格式的对象
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }

    //退出登陆
    @Override
    public Result logout(String token) {
        //从Redis中将Token的信息进行删除
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }

    //注册
    @Override
    public Result register(LoginParams loginParams) {
        /**
         * 1. 判断参数是否合法
         * 2. 判断账户是否已经存在（返回账户已经被注册）
         * 3. 注册用户
         * 4. 生成token
         * 5. 存入redis并返回
         * 6. 注意加上事务 一旦中间出现任何问题 需要回滚
         */

        //从前端参数中获取数据
        String account = loginParams.getAccount();
        String password = loginParams.getPassword();
        String nickname = loginParams.getNickname();

        //判断参数是否合法，不合法则报出异常
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password) || StringUtils.isBlank(nickname)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }

        //如果数据库中有这个用户，则报出用户已存在异常
        SysUser sysUser = this.sysUserService.findUserByAccount(account);
        if (sysUser != null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(),ErrorCode.ACCOUNT_EXIST.getMsg());
        }

        //新建用户对象并填入信息(信息可以是前端参数传送过来的，可以是自己定义的)
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+salt));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");

        //将新建用户信息保存到数据库中
        this.sysUserService.save(sysUser);

        //通过JWT创建一个token
        String token = JWTUtils.createToken(sysUser.getId());

        //将token存入redis
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);

        //给前端赋予token
        return Result.success(token);
    }
}
