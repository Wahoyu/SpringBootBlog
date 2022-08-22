package com.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {

    //传送给前端的结果类，其中包含代码信息和数据
    private boolean success;
    private int code;
    private String msg;
    private Object data;

    //成功时传送给前端的结果返回方法
    public static Result success(Object data){
        return new Result(true,200,"success",data);
    }

    //失败时传送给前端的结果返回方法
    public static Result fail(int code, String msg) {
        return new Result(false,code,msg,null);
    }
}
