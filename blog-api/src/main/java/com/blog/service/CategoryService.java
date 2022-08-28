package com.blog.service;

import com.blog.vo.CategoryVo;

public interface CategoryService {

    //显示详细文章信息时，对分类信息进行查询
    CategoryVo findCategoryById(Long categoryId);
}
