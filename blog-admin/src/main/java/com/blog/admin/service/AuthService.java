package com.blog.admin.service;

import com.blog.admin.entity.Admin;
import com.blog.admin.entity.Permission;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private AdminService adminService;

    public boolean auth(HttpServletRequest request, Authentication authentication){

        //获取你想要进行访问的路径
        String requestURI = request.getRequestURI();
        log.info("=============request url:{}==============", requestURI);

        //true代表放行 false 代表拦截
        Object principal = authentication.getPrincipal();
        if (principal == null || "anonymousUser".equals(principal)){
            //未登录
            return false;
        }
        log.info("=========登陆验证成功============");


        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();
        Admin admin = adminService.findAdminByUserName(username);
        if (admin == null){
            return false;
        }
        log.info("============2操作成功===========");


        if (admin.getId() == 1){
            //认为是超级管理员
            return true;
        }
        log.info("=============不是超级管理员==============");


        List<Permission> permissions = adminService.findPermissionsByAdminId(admin.getId());
        requestURI = StringUtils.split(requestURI,'?')[0];
        for (Permission permission : permissions) {
            if (requestURI.equals(permission.getPath())){
                log.info("=============权限通过===============");
                return true;
            }
        }
        log.info("==============没有访问权限===============");
        return false;
    }
}