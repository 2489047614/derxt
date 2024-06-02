package com.iweb.derxt.common.utils;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JwtUtil {
    public static String createJWT(long ttlMillis,Long userId,String key){
        //签名算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //创建playload的私有声明（根据特定的业务需求添加， 如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        Map<String,Object> claims = new HashMap<String,Object>();
        //生成签发人
        Map<String,Object> value = new HashMap<String,Object>();
        claims.put("userId",userId);
        JwtBuilder builder= Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())//jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(now)//签发时间
                .setSubject(JSON.toJSONString(value))//主题
                .signWith(signatureAlgorithm,key);//签名
        if(ttlMillis>=0){
            long expMillis = nowMillis+ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);//过期时间
        }
        return builder.compact();
    }
    public static Claims parseJWT(String token, String key){
        Claims claims = Jwts.parser()
                //设置签名的秘钥
                .setSigningKey(key)
                //设置需要解析的jwt
                .parseClaimsJws(token).getBody();
        return claims;
    }
}
