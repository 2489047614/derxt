package com.iweb.derxt.common.wx.config;

import lombok.Data;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class WxOpenConfig {
    @Value("wxec42a03d0976866f")
    private String loginAppid;
    @Value("ae395841b30780e0819d08d43983f386")
    private String loginSecret;
    @Value("smartpig")
    public String csrfKey;
    @Value("http://xxxx/sso/login/wxLogincallBack")
    public String redirectUrl;
    @Value("snsapi_login")
    public String scope;

    @Bean
    public WxMpService wxMpService() {
        WxMpService service = new WxMpServiceImpl();
        WxMpDefaultConfigImpl wxMpConfigStorage = new WxMpDefaultConfigImpl();
        wxMpConfigStorage.setAppId(loginAppid);
        wxMpConfigStorage.setSecret(loginSecret);
        service.setWxMpConfigStorage(wxMpConfigStorage);
        return service;
    }

}
