package com.blog.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {

    //密钥：相当于盐
    private static final String jwtToken = "123456Mszlu!@#$$";

    //生成Token
    public static String createToken(Long userId){

        //将UserId放入hashmap
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",userId);

        //生成Token并继续返回
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtToken) // 签发算法，秘钥为jwtToken
                .setClaims(claims) // body数据，要唯一，自行设置
                .setIssuedAt(new Date()) // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 60 * 1000));// 一天的有效时间
        String token = jwtBuilder.compact();
        return token;
    }

    //对Token进行验证，检测是否正确
    public static Map<String, Object> checkToken(String token){
        try {
            //对Token进行解析
            Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
            //解析成功后获取Token的Body部分（也就是B部分:用户的ID）
            return (Map<String, Object>) parse.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

}
