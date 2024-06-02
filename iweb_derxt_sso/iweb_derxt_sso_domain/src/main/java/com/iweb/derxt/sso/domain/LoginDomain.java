package com.iweb.derxt.sso.domain;

import com.iweb.derxt.common.model.CallResult;
import com.iweb.derxt.sso.domain.repository.LoginDomainRepository;
import com.iweb.derxt.sso.model.params.login.LoginParam;

public class LoginDomain {
    private LoginDomainRepository loginDomainRepository;
    private LoginParam loginParam;

    public LoginDomain(LoginDomainRepository loginDomainRepository, LoginParam loginParam) {
        this.loginDomainRepository = loginDomainRepository;
        this.loginParam = loginParam;
    }
    public CallResult<Object> getQRCodeUrl() {
        // 调用登录接口获取二维码URL
        String url = loginDomainRepository.buildQRCodeUrl();
        return CallResult.success(url);
    }
}
