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
    private Integer commentCounts;
    private Integer viewCounts;
    private Integer weight;
    //创建时间
    private String createDate;
    private String author;
    //文章详细内容
    private ArticleBodyVo body;
    private List<TagVo> tags;
    //文章分类
    private CategoryVo category;

}
