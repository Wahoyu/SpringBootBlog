package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.entity.Article;
import com.blog.mapper.ArticleMapper;
import com.blog.service.ArticleService;
import com.blog.service.TagService;
import com.blog.vo.ArticleVo;
import com.blog.vo.Result;
import com.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    TagService tagService;
    @Autowired
    SysUserServiceImpl sysUserService;

    @Override
    public Result listArticle(PageParams pageParams) {

        //通过传入的页数和页表大小创建Page对象
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());

        //创建查询用的Wrapper对象，并对wrapper对象进行限制
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate,Article::getWeight);

        //通过当前页数，页表大小，查询条件 -> 文章页
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);

        //文章页 -> 原生文章列表
        List<Article> records = articlePage.getRecords();

        // 原生文章列表 -> 精修后文章列表
        List<ArticleVo> articleVoList = copyList(records,true,true);

        //将精修的文章列表进行返回
        return Result.success(articleVoList);
    }

    //调用copy函数，挨个元素，将 原生文章列表 -> 精修后的文章列表
    private List<ArticleVo> copyList(List<Article> records, boolean isTag , boolean isAuthor){
        List<ArticleVo> articleVoList = new ArrayList<>();
        for(Article record : records){
            articleVoList.add(copy(record,isTag,isAuthor));
        }
        return articleVoList;
    }

    //BeanUtils.copyProperties使用将一个实体类对象中的一个信息完全转移到另一个实体类对象
    //判断是否需要输出tag和author进行输出
    private ArticleVo copy (Article article , boolean isTag , boolean isAuthor){
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);
        //单独设置时间
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:MM"));

        //判断是否有tag进行输出，如果有tag，向article-tag表格中获取articleid，获取tags列表放到articleVo实体类对象中
        if (isTag) {
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }

        //
        if (isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        return articleVo;
    }
}
