package com.blog.controller;

import com.blog.service.ArticleService;
import com.blog.vo.Result;
import com.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    //首页文章列表
    @PostMapping
    public Result listArticle(@RequestBody PageParams pageParams){

        //返回主页文章列表
        return articleService.listArticle(pageParams);
    }

    //首页显示最热文章
    @PostMapping("hot")
    public Result hotArticle(){

        int limit = 5;
        //返回主页文章列表
        return articleService.hotArticle(limit);
    }
    //首页显示最新文章
    @PostMapping("new")
    public Result newArticle(){

        int limit = 5;
        //返回主页文章列表
        return articleService.newArticle(limit);
    }
}
