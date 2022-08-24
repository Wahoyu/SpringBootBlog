package com.blog.service;

import com.blog.vo.Result;
import com.blog.vo.params.PageParams;

public interface ArticleService {

    //分页查询文章列表
    Result listArticle(PageParams pageParams);

    //首页显示最热文章
    Result hotArticle(int limit);

    //首页显示最新文章
    Result newArticle(int limit);
}
