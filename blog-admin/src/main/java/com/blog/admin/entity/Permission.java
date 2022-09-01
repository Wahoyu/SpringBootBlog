package com.blog.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Permission {

    //权限id
    @TableId(type = IdType.AUTO)
    private Long id;

    //权限名字
    private String name;

    //权限的路径（网址）
    private String path;

    //权限描述
    private String description;
}