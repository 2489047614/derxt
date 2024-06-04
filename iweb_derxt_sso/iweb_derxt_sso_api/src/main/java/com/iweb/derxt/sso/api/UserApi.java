package com.iweb.derxt.sso.api;

import com.iweb.derxt.common.model.CallResult;
import com.iweb.derxt.sso.model.params.user.UserParam;
import com.iweb.derxt.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserApi {
    @Autowired
    private UserService userService;
    @RequestMapping("userinfo")
    public CallResult getUserInfo(){
        UserParam userParam = new UserParam();
        return userService.userInfo(userParam);
    }
}
