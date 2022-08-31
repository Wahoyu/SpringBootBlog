package com.blog.vo.params;

import lombok.Data;

@Data
public class PageParams {

    //请求文章列表时，前端向后端传输的前端信息，用于分页
    private int page = 1;
    private int pageSize = 10;

    //请求某个分类/标签下面的文章时，用于显示传递参数
    private Long categoryId;
    private Long tagId;
}
