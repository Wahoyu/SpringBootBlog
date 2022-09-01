package com.blog.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//我们的Security类要继承这个Adapter,然后加上@Configuration注册为Bean，好让Spring可以扫描到。
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //使用BCrypt密码策略
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //生成密码
    public static void main(String[] args) {
        //加密策略 MD5 不安全 彩虹表  MD5 加盐
        String mszlu = new BCryptPasswordEncoder().encode("123456");
        System.out.println(mszlu);
    }

    //这个配置暂时不用管
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    //主要配置文件
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests() //开启登录认证

                //用户权限控制
                //.antMatchers("/user/findAll").hasRole("admin") //访问接口需要admin的角色

                //放行静态资源
                .antMatchers("/css/**").permitAll()
                .antMatchers("/img/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/plugins/**").permitAll()

                //要通过我们自定义的特殊认证（此处我们自己写认证代码，返回true就代表认证通过）
                //自定义service 来去实现实时的权限认证
                //将所有需要拦截的都放在admin下，我们拦截admin即可
                .antMatchers("/admin/**").access("@authService.auth(request,authentication)")

                //此路径下的，只要登陆成功就能访问
                .antMatchers("/pages/**").authenticated()

                //自定义登录
                .and()
                .formLogin()
                .loginPage("/login.html") //自定义的登录页面
                .loginProcessingUrl("/login") //登录处理接口
                .usernameParameter("username") //定义登录时的用户名的key 默认为username
                .passwordParameter("password") //定义登录时的密码key，默认是password
                .defaultSuccessUrl("/pages/main.html") //登陆成功后跳转
                .failureUrl("/login.html") //登陆失败后跳转
                .permitAll() //这是指和登录表单相关的接口 都通过（其实这里不生效，主要是我们只拦截admin下的，没设置这里）

                //自定义退出
                .and()
                .logout() //退出登录配置
                .logoutUrl("/logout") //退出登录接口
                .logoutSuccessUrl("/login.html")
                .permitAll() //退出登录的相关接口我们都放行（其实这里不生效，主要是我们只拦截admin下的，没设置这里）

                //拦截postman等http访问的
                .and()
                .httpBasic()

                //关闭csrf
                .and()
                .csrf().disable() //csrf关闭 如果自定义登录 需要关闭

                //支持iframe页面嵌套
                .headers().frameOptions().sameOrigin();
    }
}