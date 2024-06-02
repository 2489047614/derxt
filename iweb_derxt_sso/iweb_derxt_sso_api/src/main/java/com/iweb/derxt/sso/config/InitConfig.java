package com.iweb.derxt.sso.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@ComponentScan({"com.iweb.derxt.common.wx.config", "com.iweb.derxt.common.service"})
public class InitConfig {
}
