package com.blog.controller;

import com.blog.utils.QiniuUtils;
import com.blog.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.channels.MulticastChannel;
import java.util.UUID;

@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private QiniuUtils qiniuUtils;

    @PostMapping
    public Result upload(@RequestParam("image")MultipartFile file){

        //获取原始文件名称
        String originFilename = file.getOriginalFilename();
        //文件唯一名称 随机产生名字+小数点+获得源文件小数点后面的字符（也就是后缀）
        String fileName = UUID.randomUUID().toString()+"."+ StringUtils.substringAfterLast(originFilename,".");

        //上传文件 上传到哪儿去呢？七牛云服务器   降低我们自身应用服务器的带宽消耗

        boolean upload = qiniuUtils.upload(file,fileName);
        if(upload){
            return Result.success(QiniuUtils.url + fileName);
        }
        return Result.fail(20001,"上传失败");

    }

}
