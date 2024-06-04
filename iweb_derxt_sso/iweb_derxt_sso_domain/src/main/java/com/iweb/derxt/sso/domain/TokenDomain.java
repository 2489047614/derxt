package com.iweb.derxt.sso.domain;

import com.iweb.derxt.common.model.BusinessCodeEnum;
import com.iweb.derxt.common.model.CallResult;
import com.iweb.derxt.sso.domain.repository.TokenDomainRepository;
import org.apache.commons.lang3.StringUtils;

public class TokenDomain {
    private TokenDomainRepository tokenDomainRepository;

    public TokenDomain(TokenDomainRepository tokenDomainRepository) {
        this.tokenDomainRepository = tokenDomainRepository;
    }

    public CallResult checkToken(String token) {
        //两种解析token的方式
        //方法一 直接通过jwt工具类 解析token 获取UserId
        //方法二 从Redis中拿到token 比较是否一致如果一致 获取value userId
        String userId = tokenDomainRepository.getToken(token);
        //先判断是否已经过期了
        if (StringUtils.isBlank(userId)){
            return CallResult.fail(BusinessCodeEnum.NO_TOKEN.getCode(),BusinessCodeEnum.NO_TOKEN.getMsg());
        }
       return CallResult.success(Long.parseLong(userId));
    }




}
