package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.entity.Comment;
import com.blog.mapper.CommentMapper;
import com.blog.service.CommentsService;
import com.blog.service.SysUserService;
import com.blog.vo.CommentVo;
import com.blog.vo.Result;
import com.blog.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    SysUserService sysUserService;

    //通过文章id显示comment列表
    @Override
    public Result commentsByArticle(Long id) {
        /**
         * 1. 根据文章id从 comment表中查询到 该文章的一级评论信息列表
         * 2. 根据一级评论列表中的 一级评论者id 查询到 一级评论者的信息
         * 3. 如果id = 1  要去查询有没有子评论
         *
         *    3.1 根据父评论id 从 评论表 中查询到 该一级评论信息 的所有二级评论
         *    3.2 根据二级评论列表中的 二级评论者id 查询到 二级评论者的信息
         */

        /**
         * Vo关系：commentVo  包含  UserVo
         */

        //通过id查找  原生commentList
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, id);
        queryWrapper.eq(Comment::getLevel,1);
        List<Comment> comments = commentMapper.selectList(queryWrapper);

        //将  原生commentList  转化成  精修后的commentsVoList
        List<CommentVo> commentVoList = copyList(comments);

        //将commentVoList
        return Result.success(commentVoList);
    }

    //将commentLsit 转化成 commentVoList
    private List<CommentVo> copyList(List<Comment> comments){

        //定义一个空的commentVo列表
        List<CommentVo> commentVoList = new ArrayList<>();

        //在循环中将comment->commentVo
        for(Comment comment : comments) commentVoList.add(copy(comment));

        return commentVoList;
    }

    //将单个的comment 转化为 commentVo
    private CommentVo copy(Comment comment){

        //新建一个空的commentVo对象
        CommentVo commentVo = new CommentVo();

        //将类型的字段进行复制过来（id,content,level）
        BeanUtils.copyProperties(comment,commentVo);

        //额外添加作者信息
        Long authorId = comment.getAuthorId();
        UserVo userVo = this.sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);

        //子评论
        Integer level = comment.getLevel();
        if (1 == level){
            Long id = comment.getId();
            List<CommentVo> commentVoList = findCommentsByParentId(id);
            commentVo.setChildrens(commentVoList);
        }

        //to User 给谁评论
        if (level > 1){
            Long toUid = comment.getToUid();
            UserVo toUserVo = this.sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    //通过父评论id 查找 子评论列表
    private List<CommentVo> findCommentsByParentId(Long id){
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id);
        queryWrapper.eq(Comment::getLevel,2);
        return copyList(commentMapper.selectList(queryWrapper));
    }
}












