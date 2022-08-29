package com.blog.controller;

import com.blog.service.CategoryService;
import com.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("categorys")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //写文章的时候，获取所有的分类信息
    @GetMapping
    public Result listCategory() {
        return categoryService.findAll();
    }
}