package com.iweb.derxt.sso.domain.repository;

import com.iweb.derxt.common.constants.RedisKey;
import com.iweb.derxt.common.wx.config.WxOpenConfig;
import com.iweb.derxt.sso.domain.LoginDomain;
import com.iweb.derxt.sso.domain.UserDomain;
import com.iweb.derxt.sso.model.params.login.LoginParam;
import com.iweb.derxt.sso.model.params.user.UserParam;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class LoginDomainRepository {
    @Autowired
    public WxMpService wxMpService;
    @Autowired
    public WxOpenConfig wxOpenConfig;
    @Autowired
    public StringRedisTemplate redisTemplate;
    @Autowired
    public UserDomainRepository userDomainRepository;
    public LoginDomain createLoginDomain(LoginParam loginParam) {
        return new LoginDomain(this,loginParam);
    }

    public String buildQRCodeUrl() {
//        //使用日期 做一个登录的唯一标识
//        String date = new DateTime().toString("yyyyMMddHHmmss");
        String state = UUID.randomUUID().toString();
//        //将这个date加密一下
//        String csrfKey = DigestUtils.md5Hex(wxOpenConfig.csrfKey + date);
//        String url = wxMpService.buildQrConnectUrl(wxOpenConfig.redirectUrl,wxOpenConfig.scope, csrfKey);
        redisTemplate.opsForValue().set(RedisKey.WX_STATE_KEY+state,state,60, TimeUnit.SECONDS);
        String url = wxMpService.buildQrConnectUrl(wxOpenConfig.getRedirectUrl(), wxOpenConfig.getScope(), state);

        return url;
    }

    public boolean checkWxLoginCallBackBiz(String state){
        return redisTemplate.hasKey(RedisKey.WX_STATE_KEY+state);
    }

    public UserDomain createUserDomain(UserParam userParam){
        return userDomainRepository.createUserDomain(userParam);
    }
}
