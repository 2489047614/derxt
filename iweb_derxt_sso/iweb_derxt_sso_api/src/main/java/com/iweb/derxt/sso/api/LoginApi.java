package com.iweb.derxt.sso.api;

import com.iweb.derxt.common.model.CallResult;
import com.iweb.derxt.sso.model.params.login.LoginParam;
import com.iweb.derxt.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("login")
public class LoginApi {
    //注入服务层
    @Autowired
    private LoginService loginService;
    @ResponseBody
    @PostMapping("getQRCodeUrl")
    public CallResult getQRCodeUrl(){
        return loginService.getQRCodeUrl();
    }

    @GetMapping("wxLoginCallBack")
    public String wxLoginCallBack(HttpServletRequest request,
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
        //return loginService.wxLoginCallBack(loginParam);
        CallResult callResult = loginService.wxLoginCallBack(loginParam);
        //登录回调成功之后 我们注意一下 你应该有一个重定向的页面
        if (callResult.isSuccess()){
            return "redirect:http://www.lzxtedu.com/course";
        }else {
            return "redirect:http://www.lzxtedu.com";
        }
    }
}











