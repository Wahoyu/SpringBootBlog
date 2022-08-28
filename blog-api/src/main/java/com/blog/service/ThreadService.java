package com.blog.service;

import com.blog.entity.Article;
import com.blog.mapper.ArticleMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {

    //期望此线程在线程池执行 ， 不影响原有的主线程
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper , Article article){

        try{
            Thread.sleep(5000);
            System.out.println("更新完成...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
