package com.iweb.derxt.sso.domain;

import com.iweb.derxt.common.constants.RedisKey;
import com.iweb.derxt.common.model.BusinessCodeEnum;
import com.iweb.derxt.common.model.CallResult;
import com.iweb.derxt.common.utils.JwtUtil;
import com.iweb.derxt.sso.data.User;
import com.iweb.derxt.sso.domain.repository.LoginDomainRepository;
import com.iweb.derxt.sso.model.params.login.LoginParam;
import com.iweb.derxt.sso.model.params.user.UserParam;
import com.iweb.derxt.sso.model.enums.LoginType;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

public class LoginDomain {
    private LoginDomainRepository loginDomainRepository;
    private LoginParam loginParam;

    //设置一个jwt加密秘钥
    private String secretKey = "iwebderxt@#$%^&*@";

    public LoginDomain(LoginDomainRepository loginDomainRepository, LoginParam loginParam) {
        this.loginDomainRepository = loginDomainRepository;
        this.loginParam = loginParam;
    }

    public CallResult<Object> getQRCodeUrl() {
        // 调用登录接口获取二维码URL
        String url = loginDomainRepository.buildQRCodeUrl();
        return CallResult.success(url);
    }

    //校验state
    public CallResult<Object> checkWxLoginCallBackBiz() {
        boolean isAlive = loginDomainRepository.checkWxLoginCallBackBiz(loginParam.getState());
        if (!isAlive) {
            return CallResult.fail(BusinessCodeEnum.CHECK_BIZ_NO_RESULT.getCode(), BusinessCodeEnum.CHECK_BIZ_NO_RESULT.getMsg());
        }
        return CallResult.success();
    }

    public CallResult<Object> wxLoginCallBack() {
        //先获取code
        String code = loginParam.getCode();
        //先从Redis中看一下 有没有refresh_token 其实就是看我之前有没有登录过
        String refreshToken = loginDomainRepository.redisTemplate.opsForValue().get(RedisKey.REFRESH_TOKEN);
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
        //如果refreshToken==null才能进行下面的步骤
        try {
            if (refreshToken == null) {

                //1.通过code获得access_token
                wxMpOAuth2AccessToken = loginDomainRepository.wxMpService.oauth2getAccessToken(code);
                //2.通过access_token获取refresh_token
                refreshToken = wxMpOAuth2AccessToken.getRefreshToken();
                //3.将refresh_token存入Redis中 设置28或者29天有效期
                loginDomainRepository.redisTemplate.opsForValue().set(RedisKey.REFRESH_TOKEN, refreshToken, 28, TimeUnit.DAYS);
            } else {
                //使用refreshtoken刷新一下access_token
                wxMpOAuth2AccessToken = loginDomainRepository.wxMpService.oauth2refreshAccessToken(refreshToken);
            }
            //4.接下来access_token 得到了就可以使用它获取用户的信息了
            WxMpUser wxMpUser = loginDomainRepository.wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, "zh_CN");
            //5.获取用户的unionId以此判断用户咋希望ode数据库中是否登录过
            String unionId = wxMpUser.getUnionId();
            //到数据库中去查找
            UserParam userParam = new UserParam().findUserByUnionId(unionId);
            User user = loginDomainRepository.createUserDomain(userParam);
            //如果user存在说明我们登录过 只需要更新最后登录时间就可以了
            //如果user不存在 那么说明没有登录过 还需要添加用户信息
            boolean isNew = false;
            if (user == null) {
                //注册用户信息
                user = new User();
                Long currentTime = System.currentTimeMillis();
                user.setNickname(wxMpUser.getNickname());
                user.setHeadImageUrl(wxMpUser.getHeadImgUrl());
                user.setSex(wxMpUser.getSex());
                user.setOpenid(wxMpUser.getOpenId());
                user.setLoginType(LoginType.WX.getCode());
                user.setCountry(wxMpUser.getCountry());
                user.setCity(wxMpUser.getCity());
                user.setProvince(wxMpUser.getProvince());
                user.setRegisterTime(currentTime);
                user.setLastLoginTime(currentTime);
                user.setUnionId(wxMpUser.getUnionId());
                user.setArea("");
                user.setMobile("");
                user.setGrade("");
                user.setName(wxMpUser.getNickname());
                user.setSchool("");
                this.loginDomainRepository.createUserDomain(new UserParam().saveUser(user));
                isNew = true;
            }
            //解下来我们应该给这次登录产生一个token
            //给三个参数 第一个是过期时间 第二个是userId第三个事秘钥
            String token = JwtUtil.createJWT(7 * 24 * 60 * 60 * 1000, user.getId(), secretKey);
            //存储到Redis里面设置七天过期
            loginDomainRepository.redisTemplate.opsForValue().set(RedisKey.TOKEN + token, String.valueOf(user.getId()), 7, TimeUnit.DAYS);
            //因为有付费课程 所以 我们不允许 多设备登录
            //先判断一下 有没有人已经在其他设备登录了
            String oldToken = loginDomainRepository.redisTemplate.opsForValue().get(RedisKey.LOGIN_USER_TOKEN + user.getId());
            if (oldToken != null) {
                //说明有其他设备登录了 需要将之前登录的设备下线
                loginDomainRepository.redisTemplate.delete(RedisKey.TOKEN + token);
            }
            //重新设置token
            loginDomainRepository.redisTemplate.opsForValue().set(RedisKey.LOGIN_USER_TOKEN + user.getId(), token, 7, TimeUnit.DAYS);
            //接下来将token返回给前端 放到cookie里面 以便后续在校验 请求头传递
            HttpServletResponse response = loginParam.getResponse();
            Cookie cookie = new Cookie("t_token", token);
            cookie.setMaxAge(8 * 24 * 3600);
            cookie.setPath("/");
            response.addCookie(cookie);
            //后续可以通过异步或者线程池实现积分的累加
            //包括登录的日志可以实现
            //如果不是新用户 更新最后的登录时间
            if (!isNew) {
                user.setLastLoginTime(System.currentTimeMillis());
                this.loginDomainRepository.createUserDomain(new UserParam().updateUser(user));
            }
            return CallResult.success();
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        //授权失败
        return CallResult.fail(BusinessCodeEnum.LOGIN_WX_NOT_USER_INFO.getCode(), BusinessCodeEnum.LOGIN_WX_NOT_USER_INFO.getMsg());

    }


}
