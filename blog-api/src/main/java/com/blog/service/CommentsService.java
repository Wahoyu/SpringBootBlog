package com.blog.service;

import com.blog.vo.Result;

public interface CommentsService {

    //通过文章id查找commentList
    Result commentsByArticle(Long id);

}
