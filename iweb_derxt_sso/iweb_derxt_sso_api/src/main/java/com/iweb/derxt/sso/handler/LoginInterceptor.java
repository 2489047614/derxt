package com.iweb.derxt.sso.handler;

import com.alibaba.fastjson.JSON;
import com.iweb.derxt.common.login.UserThreadLocal;
import com.iweb.derxt.common.model.BusinessCodeEnum;
import com.iweb.derxt.common.model.CallResult;
import com.iweb.derxt.sso.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//登录拦截器
@Component//将类注入给ioc
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("---------------login Interceptor start------------");
        log.info("request url:{}",request.getRequestURL());
        log.info("request method:{}",request.getMethod());
        log.info("---------------login Interceptor end------------");
        //获取cookie中的token
        //当然 面试的时候可以说 是从请求头中获取的 请求头携带token 是前端的配置
        Cookie[] cookies = request.getCookies();
        //先判断是否有cookie
        if (cookies==null){
            //前端就没有缓存 但是 一般不需要
            //实际场景 不太需要 因为我们后端有session 所以前端一定有cookie
        }
        String token="";
        for (Cookie cookie:cookies){
            String name = cookie.getName();
            if (name.equals("t_token")){
                token= cookie.getValue();
            }
        }
        //接下来 判断token是否过期了 是否为null
        if (StringUtils.isBlank(token)){
            //说明token是""或者是null
            returnJson(response);
            return false;//拦截下来 不予放行
        }
        //走到这里说明token是有的 但是不一定合法
        //所以 还需要去解析token
        //Long
        Long userId = tokenService.checkToken(token);
        //判断userId的合法性
        if (userId==null){
            returnJson(response);
            return false;
        }
        //如果都没问题了 我们就可以将数据存储在ThreadLocal里面
        //当然你也可以存储在redis里面 或者session里面
        //redis的弊端 内存读写 仍然没有线程内存卡 有网络IO的开销
        //session 如果是集群呢？ 缓存不一致问题
        UserThreadLocal.put(userId);
        return true;
    }

    private void returnJson(HttpServletResponse response) {
        PrintWriter writer =null;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            CallResult<Object> callResult = CallResult.fail(BusinessCodeEnum.NO_LOGIN.getCode(), BusinessCodeEnum.NO_LOGIN.getMsg());
            writer.print(JSON.toJSONString(callResult));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (writer!=null){
                writer.close();
            }
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //因为我们使用了ThreadLocal 所以在线程结束之前 要清空ThreadLocal
        //否则可能会产生内存泄露的问题
        UserThreadLocal.remove();
    }
}
