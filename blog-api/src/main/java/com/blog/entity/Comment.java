package com.blog.entity;

import lombok.Data;

@Data
public class Comment {
    //评论的id
    private Long id;
    //评论的内容
    private String content;
    //创建时间
    private Long createDate;
    //文章的id
    private Long articleId;  //不显示于CommentVo中
    //父评论的id
    private Long parentId;   //不显示于CommentVo中
    //作者id
    private Long authorId;
    //艾特用户
    private Long toUid;
    //是第几层评论  1是文章的评论  2是评论的评论
    private Integer level;
}