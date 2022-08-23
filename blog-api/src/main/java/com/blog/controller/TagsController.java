package com.blog.controller;

import com.blog.service.TagService;
import com.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
}
