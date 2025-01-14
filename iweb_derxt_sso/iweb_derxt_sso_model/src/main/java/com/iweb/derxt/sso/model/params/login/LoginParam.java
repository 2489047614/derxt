package com.iweb.derxt.sso.model.params.login;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Data
public class LoginParam {

    private String username;
    private String password;

    //wx回调的传参
    private String code;
    private String state;
    private HttpServletResponse response;
    private HttpServletRequest request;

    private String cookieUserId;
}
