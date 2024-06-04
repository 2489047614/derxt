package com.iweb.derxt.sso.domain;

import com.iweb.derxt.common.login.UserThreadLocal;
import com.iweb.derxt.common.model.CallResult;
import com.iweb.derxt.sso.data.User;
import com.iweb.derxt.sso.domain.repository.UserDomainRepository;
import com.iweb.derxt.sso.model.params.user.UserParam;
import com.iweb.derxt.sso.model.vo.UserModel;

public class UserDomain extends User {
    private UserDomainRepository userDomainRepository;
    private UserParam userParam;

    public UserDomain(UserDomainRepository userDomainRepository, UserParam userParam) {
        this.userDomainRepository = userDomainRepository;
        this.userParam = userParam;
    }
    public User findUserByUnionId(String unionId){
        return userDomainRepository.findUserByUnionId(unionId);
    }
    public void saveUser(User user){
        userDomainRepository.saveUser(user);
    }
    public void updateUser(User user){
        userDomainRepository.updateUser(user);
    }

    public CallResult<Object> getUserInfo(){
        //从ThreadLocal中获取userId
        Long userId = UserThreadLocal.get();
        //从数据库中查询一下user对象
        User user = userDomainRepository.findUserById(userId);
        //model 和 viewModel
        //model 是我们从数据库中直接查询获取的数据
        //viewModel 是我们需要给前端渲染的数据
        //两者 很大程度上会有一点不一致
        UserModel userModel = new UserModel();
        userModel.setCity(user.getCity());
        userModel.setCountry(user.getCountry());
        userModel.setHeadImgUrl(user.getHeadImageUrl());
        userModel.setSex(user.getSex());
        userModel.setProvince(user.getProvince());
        userModel.setMobile(user.getMobile());
        userModel.setArea(userModel.getArea());
        userModel.setGrade(user.getGrade());
        userModel.setName(user.getName());
        userModel.setSchool(user.getSchool());
        return CallResult.success(userModel);
    }

}







