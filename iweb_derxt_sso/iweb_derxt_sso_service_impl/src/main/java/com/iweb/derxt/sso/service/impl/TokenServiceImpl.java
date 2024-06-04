package com.iweb.derxt.sso.service.impl;

import com.iweb.derxt.common.model.CallResult;
import com.iweb.derxt.common.service.AbstractTemplateAction;
import com.iweb.derxt.sso.domain.TokenDomain;
import com.iweb.derxt.sso.domain.repository.TokenDomainRepository;
import com.iweb.derxt.sso.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl extends AbstractService implements TokenService {
    @Autowired
    private TokenDomainRepository tokenDomainRepository;
    @Override
    public Long checkToken(String token) {
        TokenDomain tokenDomain = tokenDomainRepository.createTokenDomain();
        //因为解析token 所以不需要带有事务 去执行
        CallResult<Object> callResult = this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            //因为没有参数所以不需要checkParam checkBiz
            @Override
            public CallResult<Object> doAction() {
                return tokenDomain.checkToken(token);
            }
        });
        return (Long) callResult.getResult();
    }
}




