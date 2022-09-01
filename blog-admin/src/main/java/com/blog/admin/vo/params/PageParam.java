package com.blog.admin.vo.params;

import lombok.Data;

@Data
public class PageParam {

    //当前页
    private Integer currentPage;

    //页面大小
    private Integer pageSize;

    //前端输入的权限名称（查询条件）
    private String queryString;
}