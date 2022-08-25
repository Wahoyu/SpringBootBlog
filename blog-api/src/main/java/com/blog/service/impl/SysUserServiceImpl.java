package com.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.blog.entity.SysUser;
import com.blog.mapper.SysUserMapper;
import com.blog.service.SysUserService;
import com.blog.utils.JWTUtils;
import com.blog.vo.ErrorCode;
import com.blog.vo.LoginUserVo;
import com.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
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

        //1.通过JWT对token进行合法性校验，不合格则报错
        Map<String, Object> map = JWTUtils.checkToken(token);
        if (map == null){
            return Result.fail(ErrorCode.NO_LOGIN.getCode(),ErrorCode.NO_LOGIN.getMsg());
        }

        //我们当初进行Redis存储时，key是token，value是JSON.toJSONString(sysUser)
        //通过JWT格式检查之后，在Redis中进行查出token对应的sysuser的json值，没有则报错
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userJson)){
            return Result.fail(ErrorCode.NO_LOGIN.getCode(),ErrorCode.NO_LOGIN.getMsg());
        }

        //通过json解析器，将userJson信息转化成SysUser格式的对象
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);

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

}
