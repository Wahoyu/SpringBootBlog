package com.blog.config;

import com.blog.handler.LoginInterceptor;
import com.blog.vo.params.LoginParams;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig  implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    //前后端跨域配置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //跨域配置，不可设置为*，不安全, 前后端分离项目，可能域名不一致
        //本地测试 端口不一致 也算跨域 允许8080端口进行访问
        registry.addMapping("/**").allowedOrigins("http://localhost:8080");
    }

    //配置访问限制
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截test接口 后续遇到真正需要限制的接口时，再配置真正的接口
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/test").addPathPatterns("/comments/create/change");
    }
}
