package com.blog.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.blog.entity.SysUser;
import com.blog.service.LoginService;
import com.blog.vo.ErrorCode;
import com.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    //我们需要重写其中的prehandler实现（意思是在调用Controller的方法之前进行执行）
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 1. 需要判断 请求的接口路径 是否为 HandlerMethod（controller方法）,如果是访问静态资源的RequestResourceHandler，应该放行
         * 2. 判断 token 是否为空（如果为空：未登录
         * 3. 如果token不为空：进行登陆验证checkToken）
         * 4. 如果验证成功 运行即可
         */

        //放行 访问静态资源的 RequestResourceHandler
        if (!(handler instanceof HandlerMethod)){
            return true;
        }

        //判断token是否为空
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)){
            //返回json格式的错误信息
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(),"未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }

        //认证token
        SysUser sysUser = loginService.checkToken(token);
        if(sysUser == null){
            //返回json格式的错误信息
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(),"未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }

        //登陆验证成功，放行
        return true;
    }
}
