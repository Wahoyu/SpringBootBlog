package com.blog.vo;

import lombok.Data;

import java.util.List;

@Data
public class ArticleVo {

    //文章id
    private Long id;
    //文章标题
    private String title;
    //主页文章简介
    private String summary;
    private int commentCounts;
    private int viewCounts;
    private int weight;
    //创建时间
    private String createDate;
    private String author;
//    private ArticleBodyVo body;
    private List<TagVo> tags;
//    private List<CategoryVo> categorys;

}
