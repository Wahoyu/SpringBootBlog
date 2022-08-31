package com.blog.controller;

import com.blog.common.cache.Cache;
import com.blog.service.ArticleService;
import com.blog.vo.Result;
import com.blog.vo.params.ArticleParam;
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
    @Cache(expire = 5 * 60 * 1000,name = "hot_article")
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

    //显示首页文章归档
    @PostMapping("listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }

    //显示文章详细信息
    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);
    }

    //写文章
    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }
}
