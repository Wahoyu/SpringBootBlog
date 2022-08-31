package com.blog.controller;

import com.blog.service.TagService;
import com.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags")
public class TagsController {

    @Autowired
    TagService tagService;

    //显示6个最热tags
    @GetMapping("hot")
    public Result hot(){
        int limit = 6;
        return tagService.hots(limit);
    }

    //写文章时 显示所有的tags
    @GetMapping
    public Result findAll(){
        return tagService.findAll();
    }

    //主页-显示标签页面
    @GetMapping("detail")
    public Result findAllDetail(){
        return tagService.findAllDetail();
    }

    //展示某个标签的全部文章
    @GetMapping("detail/{id}")
    public Result findDetailById(@PathVariable("id") Long id){
        return tagService.findDetailById(id);
    }

}
