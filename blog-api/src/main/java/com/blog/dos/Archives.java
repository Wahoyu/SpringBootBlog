package com.blog.dos;

import lombok.Data;

//用于展示首页文章归档
@Data
public class Archives {

    private Integer year;

    private Integer month;

    private Integer count;
}
