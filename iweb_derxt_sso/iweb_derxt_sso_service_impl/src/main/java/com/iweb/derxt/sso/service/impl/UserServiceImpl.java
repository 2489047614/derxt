package com.iweb.derxt.sso.service.impl;

import com.iweb.derxt.common.model.CallResult;
import com.iweb.derxt.common.service.AbstractTemplateAction;
import com.iweb.derxt.sso.domain.UserDomain;
import com.iweb.derxt.sso.domain.repository.UserDomainRepository;
import com.iweb.derxt.sso.model.params.user.UserParam;
import com.iweb.derxt.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends AbstractService implements UserService {
    @Autowired
    private UserDomainRepository userDomainRepository;
    @Override
    public CallResult userInfo(UserParam userParam) {
        UserDomain userDomain = userDomainRepository.createUserDomain(userParam);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return userDomain.getUserInfo();
            }
        });
    }
}
