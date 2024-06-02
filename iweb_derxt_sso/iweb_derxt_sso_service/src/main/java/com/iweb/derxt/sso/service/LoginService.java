package com.iweb.derxt.sso.service;

import com.iweb.derxt.common.model.CallResult;
import com.iweb.derxt.sso.model.params.login.LoginParam;

public interface LoginService {
    CallResult getQRCodeUrl();

    CallResult wxLoginCallBack(LoginParam loginParam);
}
