package com.blog.common.cache;

import com.alibaba.fastjson.JSON;
import com.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

@Aspect
@Component
@Slf4j
public class CacheAspect {

    //导入redis应用类
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //定义了切点
    @Pointcut("@annotation(com.blog.common.cache.Cache)")
    public void pt(){}

    //环绕通知：可以在方法的前后进行增强
    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp){


        try {
            /**
             * 获取类名和方法名
             */
            Signature signature = pjp.getSignature();
            //1.获取类名
            String className = pjp.getTarget().getClass().getSimpleName();
            //2.获取方法名
            String methodName = signature.getName();

            /**
             * 开始获取参数
             *
             */
            //先定义空数组A
            Class[] parameterTypes = new Class[pjp.getArgs().length];
            //获取切点方法传入的参数
            Object[] args = pjp.getArgs();
            //定义空参数P
            String params = "";
            //对切点的参数进行循环判断
            //如果有值则存到parameterTypes中并转化为json(因为参数很可能是一个类)
            for(int i=0; i<args.length; i++) {
                if(args[i] != null) {
                    params += JSON.toJSONString(args[i]);

                    //为了下面拿到我们的方法Method
                    parameterTypes[i] = args[i].getClass();
                }else {
                    parameterTypes[i] = null;
                }
            }

            /**
             * 为redis的key做准备（我们输入的缓存名称name+类名className+方法名methodName+JSON版本的参数params）
             */
            //参数转化成JSON后进行MD5加密
            if (StringUtils.isNotEmpty(params)) {
                //加密 以防出现key过长以及字符转义获取不到的情况
                params = DigestUtils.md5Hex(params);
            }
            //为了获取到我们加的cache注解
            Method method = pjp.getSignature().getDeclaringType().getMethod(methodName, parameterTypes);
            //获取Cache注解
            Cache annotation = method.getAnnotation(Cache.class);
            //缓存过期时间
            long expire = annotation.expire();
            //缓存名称
            String name = annotation.name();


            /**
             * 获取数据的时候，我们先生成key,通过key再生成Value进行查找
             * 如果我们获取到了数据，且不为空，那么我们就算是获取成功
             */
            //生成key
            String redisKey = name + "::" + className+"::"+methodName+"::"+params;
            //通过key查找value
            String redisValue = redisTemplate.opsForValue().get(redisKey);
            //如果不为空，那我们就算是拿到缓存了
            if (StringUtils.isNotEmpty(redisValue)){
                log.info("获取到缓存===========,{},{}",className,methodName);
                return JSON.parseObject(redisValue, Result.class);
            }

            /**
             * 如果我们拿到的是空值，那么抱歉，你得去磁盘中读取数据，获取后还得把数据存一下，存入缓存
             */
            Object proceed = pjp.proceed();
            redisTemplate.opsForValue().set(redisKey,JSON.toJSONString(proceed), Duration.ofMillis(expire));
            log.info("刚才没有缓存，现在有了,{},{}",className,methodName);
            return proceed;

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return Result.fail(-999,"系统错误");
    }

}