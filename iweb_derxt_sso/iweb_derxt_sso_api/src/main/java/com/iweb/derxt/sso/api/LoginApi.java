package com.iweb.derxt.sso.api;

import com.iweb.derxt.common.model.CallResult;
import com.iweb.derxt.sso.model.params.login.LoginParam;
import com.iweb.derxt.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("login")
public class LoginApi {
    //注入服务层
    @Autowired
    private LoginService loginService;

    @PostMapping("getQRCodeUrl")
    public CallResult getQRCodeUrl(){
        return loginService.getQRCodeUrl();
    }

    @GetMapping("wxLoginCallBack")
    public CallResult wxLoginCallBack(HttpServletRequest request,
                                        HttpServletResponse response,
                                        String code,
                                        String state){
        //为了统一参数 对象 所以需要将所有的对象 装进loginParam
        LoginParam loginParam = new LoginParam();
        loginParam.setCode(code);
        loginParam.setState(state);
        loginParam.setRequest(request);
        loginParam.setResponse(response);
        //返回服务层
        return loginService.wxLoginCallBack(loginParam);
    }
}
