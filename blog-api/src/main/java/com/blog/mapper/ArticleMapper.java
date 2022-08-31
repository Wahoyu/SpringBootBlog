package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.dos.Archives;
import com.blog.entity.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    //显示主页文章归档
    List<Archives> listArchives();

    //获取全部文章列表 或 某分类的文章列表 或 某标签的文章列表 或 归档文章列表
    IPage<Article> listArticle(Page<Article> page, Long categoryId, Long tagId, String year, String month);
}
