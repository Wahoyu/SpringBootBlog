package com.blog.common.cache;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {

    //过期时间
    long expire() default 1 * 60 * 1000;

    //缓存标识，自定义缓存的前缀
    String name() default "";

}