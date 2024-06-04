package com.iweb.derxt.sso.service;

import com.iweb.derxt.common.model.CallResult;
import com.iweb.derxt.sso.model.params.user.UserParam;

public interface UserService {
    CallResult userInfo(UserParam userParam);
}
