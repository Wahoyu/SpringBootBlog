package com.blog.service;

import com.blog.vo.Result;
import com.blog.vo.TagVo;

import java.util.List;

public interface TagService {

    //通过文章id查询到标签列表
    List<TagVo> findTagsByArticleId(Long articleId);

    //显示最热标签
    Result hots(int limit);

    //显示所有的tags
    Result findAll();
}
