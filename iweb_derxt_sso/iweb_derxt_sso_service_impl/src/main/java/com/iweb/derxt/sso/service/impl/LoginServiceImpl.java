package com.iweb.derxt.sso.service.impl;

import com.iweb.derxt.common.service.AbstractTemplateAction;
import com.iweb.derxt.common.wx.config.WxOpenConfig;
import com.iweb.derxt.common.model.CallResult;
import com.iweb.derxt.sso.domain.LoginDomain;
import com.iweb.derxt.sso.domain.UserDomain;
import com.iweb.derxt.sso.domain.repository.LoginDomainRepository;
import com.iweb.derxt.sso.model.params.login.LoginParam;
import com.iweb.derxt.sso.service.LoginService;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl extends AbstractService implements LoginService {
    @Autowired
    private LoginDomainRepository loginDomainRepository;

    @Override
    public CallResult getQRCodeUrl() {
        LoginDomain loginDomain = loginDomainRepository.createLoginDomain(new LoginParam());
        return this.serviceTemplate.executeQuery(
                new AbstractTemplateAction<Object>() {
                    @Override
                    public CallResult<Object> doAction() {
                        return loginDomain.getQRCodeUrl();
                    }
                });
    }

    @Override
    public CallResult wxLoginCallBack(LoginParam loginParam) {
        LoginDomain loginDomain = loginDomainRepository.createLoginDomain(loginParam);
        //在回调中如果第一次执行那么需要注意从微信获取用户之后添加到本地数据库
        return this.serviceTemplate.execute(
                new AbstractTemplateAction<Object>() {
                    //为什么有参数 但是不需要参数校验
                    //因为request response不需要校验
                    //code state code是你允许登录的时候产生的 state是二维码的回调参数

                    //检查 我们自己生成的state和我们回调的state是否一致
                    @Override

                    public CallResult<Object> checkBiz() {
                        return loginDomain.checkWxLoginCallBackBiz();
                    }
                    @Override
                    public CallResult<Object> doAction() {
                        return loginDomain.wxLoginCallBack();
                    }
                });

    }
}
