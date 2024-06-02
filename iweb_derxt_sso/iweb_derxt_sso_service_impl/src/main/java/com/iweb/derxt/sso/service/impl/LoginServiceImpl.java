package com.iweb.derxt.sso.service.impl;

import com.iweb.derxt.common.service.AbstractTemplateAction;
import com.iweb.derxt.common.wx.config.WxOpenConfig;
import com.iweb.derxt.common.model.CallResult;
import com.iweb.derxt.sso.domain.LoginDomain;
import com.iweb.derxt.sso.domain.repository.LoginDomainRepository;
import com.iweb.derxt.sso.model.params.login.LoginParam;
import com.iweb.derxt.sso.service.LoginService;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl extends AbstractService implements LoginService {
    @Autowired
    private LoginDomainRepository loginDomainRepository;
    @Override
    public CallResult getQRCodeUrl() {
        LoginDomain loginDomain =loginDomainRepository.createLoginDomain(new LoginParam());
        return this.serviceTemplate.executeQuery(
                new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return loginDomain.getQRCodeUrl();
            }
        });
    }

    @Override
    public CallResult wxLoginCallBack(LoginParam loginParam) {
        return null;
    }
}
