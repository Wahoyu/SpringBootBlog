package com.blog.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.admin.entity.Permission;
import com.blog.admin.mapper.PermissionMapper;
import com.blog.admin.vo.PageResult;
import com.blog.admin.vo.Result;
import com.blog.admin.vo.params.PageParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Wahoyu
 */
@Service
public class PermissionService{

    @Autowired
    private PermissionMapper permissionMapper;

    //显示权限列表
    //传入分页所需的参数，获取列表
    public Result listPermission(PageParam pageParam){

        //获取 当前页 和 页容量 生成page对象
        Page<Permission> page = new Page<>(pageParam.getCurrentPage(),pageParam.getPageSize());

        //创建Wrapper对象
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();

        //如果我们在前端输入了查询名称，那么我们就显示这个就OK了
        if (StringUtils.isNotBlank(pageParam.getQueryString())) {
            queryWrapper.eq(Permission::getName,pageParam.getQueryString());
        }

        //获取到查询到的页
        Page<Permission> permissionPage = this.permissionMapper.selectPage(page, queryWrapper);

        //创建一个返回给前端的分页结果，将我们查到的分页信息放入其中
        PageResult<Permission> pageResult = new PageResult<>();
        pageResult.setList(permissionPage.getRecords());
        pageResult.setTotal(permissionPage.getTotal());

        //将pageResult装入Result中返回到Controller
        return Result.success(pageResult);
    }

    //添加权限
    public Result add(Permission permission) {
        this.permissionMapper.insert(permission);
        return Result.success(null);
    }

    //增加权限
    public Result update(Permission permission) {
        this.permissionMapper.updateById(permission);
        return Result.success(null);
    }

    //删除权限
    public Result delete(Long id) {
        this.permissionMapper.deleteById(id);
        return Result.success(null);
    }
}