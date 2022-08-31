package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.common.aop.LogAnnotation;
import com.blog.dos.Archives;
import com.blog.dos.ArticleTag;
import com.blog.entity.Article;
import com.blog.entity.ArticleBody;
import com.blog.entity.SysUser;
import com.blog.mapper.ArticleBodyMapper;
import com.blog.mapper.ArticleMapper;
import com.blog.mapper.ArticleTagMapper;
import com.blog.service.ArticleService;
import com.blog.service.CategoryService;
import com.blog.service.TagService;
import com.blog.service.ThreadService;
import com.blog.utils.UserThreadLocal;
import com.blog.vo.ArticleBodyVo;
import com.blog.vo.ArticleVo;
import com.blog.vo.Result;
import com.blog.vo.TagVo;
import com.blog.vo.params.ArticleParam;
import com.blog.vo.params.PageParams;
import com.sun.org.apache.xpath.internal.objects.XBoolean;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    ThreadService threadService;
    @Autowired
    ArticleTagMapper articleTagMapper;
    @Autowired
    ArticleBodyMapper articleBodyMapper;
    @Autowired
    CategoryService categoryService;

    //获取全部文章列表 或 某分类的文章列表 或 某标签的文章列表 或 归档文章列表
    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        IPage<Article> articleIPage = this.articleMapper.listArticle(page,pageParams.getCategoryId(),pageParams.getTagId(),pageParams.getYear(),pageParams.getMonth());
        return Result.success(copyList(articleIPage.getRecords(),true,true));
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

        //如果文章有对应作者，则显示
        if (isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        return articleVo;
    }

    //这是针对文章详细内容的ArticleVo-copy方法，为文章列表的copy方法之重载版本
    private ArticleVo copy (Article article , boolean isTag , boolean isAuthor,boolean isBody ,boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);
        //单独设置时间
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:MM"));

        //判断是否有tag进行输出，如果有tag，向article-tag表格中获取articleid，获取tags列表放到articleVo实体类对象中
        if (isTag) {
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }

        //如果文章有对应作者，则显示
        if (isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }

        //如果需要body信息，要进行查询
        if (isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleByBodyId(bodyId));
        }

        //如果需要category信息，要进行查询
        if (isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }

        return articleVo;
    }

    //查询文章详细信息（文章内容）
    private ArticleBodyVo findArticleByBodyId(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

    //首页显示最热文章
    @Override
    public Result hotArticle(int limit) {
        //创建条件构造器wrapper对象
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //通过文章的观看次数进行降序排序
        queryWrapper.orderByDesc(Article::getViewCounts);
        //限制查询id和title
        queryWrapper.select(Article::getId,Article::getTitle);
        //查询语句的结尾
        queryWrapper.last("limit "+limit);
        //sql: select id ,title from article order by view_counts desc limit 5

        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }

    //首页显示最新文章
    @Override
    public Result newArticle(int limit) {
        //创建Wrapper对象
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //通过文章的观看次数进行降序排序
        queryWrapper.orderByDesc(Article::getCreateDate);
        //限制查询id和title
        queryWrapper.select(Article::getId,Article::getTitle);
        //查询语句的结尾
        queryWrapper.last("limit "+limit);
        //sql: select id ,title from article order by view_counts desc limit 5

        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }

    //首页显示文章归档
    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    //显示文章详细信息（内容加标签分类等全部）
    @LogAnnotation(module = "文章",operation = "获取文章列表")
    @Override
    public Result findArticleById(Long articleId) {

        //先通过id创造出一个包含简单信息的article对象
        Article article = this.articleMapper.selectById(articleId);

        //将简单article 转化为 简单articleVo
        ArticleVo articleVo = copy(article,true,true,true,true);

        //模拟更新操作
        threadService.updateArticleViewCount(articleMapper,article);

        //将简单Vo进行返回
        return Result.success(articleVo);
    }

    //写文章
    @Override
    //添加事务注解
    @Transactional
    public Result publish(ArticleParam articleParam) {

        //从内存中获取了用户
        SysUser sysUser = UserThreadLocal.get();

        //创建article对象，存储到article表
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setCategoryId(articleParam.getCategory().getId());
        article.setCreateDate(System.currentTimeMillis());
        article.setCommentCounts(0);
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        article.setBodyId(-1L);
        this.articleMapper.insert(article);

        //从参数中获取tag,存储到article--tag表
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());
                this.articleTagMapper.insert(articleTag);
            }
        }

        //创建articleBody表，存储到ArticleBody表中，并可以存储到article对象中
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);
        //将articleBody对象存储到article对象中
        article.setBodyId(articleBody.getId());

        //存储articleMapper
        articleMapper.updateById(article);

        //创建articleVo，设置Id，进行返回
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId());
        return Result.success(articleVo);
    }
}
