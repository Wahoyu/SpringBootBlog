package com.blog.common.aop;

import java.lang.annotation.*;

/**
 * 自定义注解+AOP实现日志记录
 * 自定义注解：使用元注解定义注解
 * @author Wahoyu
 */

@Target(ElementType.METHOD)  //该注解可放在方法上面
@Retention(RetentionPolicy.RUNTIME)  //运行时注解
@Documented    //可生成文档
public @interface LogAnnotation {

    String module() default "";   //定义了注解的参数、参数的默认值
    String operation() default "";
}