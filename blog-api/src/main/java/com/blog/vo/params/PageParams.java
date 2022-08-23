package com.blog.vo.params;

import lombok.Data;

@Data
public class PageParams {

    //请求文章列表时，前端向后端传输的前端信息，用于分页
    private int page = 1;
    private int pageSize = 10;
}
