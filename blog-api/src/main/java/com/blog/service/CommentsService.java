package com.blog.service;

import com.blog.vo.Result;
import com.blog.vo.params.CommentParam;

public interface CommentsService {

    //通过文章id查找commentList
    Result commentsByArticle(Long id);

    //写评论
    Result comment(CommentParam commentParam);
}
