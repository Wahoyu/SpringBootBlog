package com.blog.admin.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {

    //返回给前端的列表
    private List<T> list;

    //total不知道是什么，可能是总数？
    private Long total;
}