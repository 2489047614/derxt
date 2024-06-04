package com.iweb.derxt.sso.domain.repository;

import com.iweb.derxt.common.constants.RedisKey;
import com.iweb.derxt.sso.domain.TokenDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class TokenDomainRepository {
    @Autowired
    private StringRedisTemplate redisTemplate;
    public TokenDomain createTokenDomain(){
        return new TokenDomain(this);
    }
    public String getToken(String token){
        return redisTemplate.opsForValue().get(RedisKey.TOKEN+token);
    }
}
