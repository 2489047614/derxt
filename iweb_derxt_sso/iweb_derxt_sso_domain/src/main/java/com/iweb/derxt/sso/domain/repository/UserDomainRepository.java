package com.iweb.derxt.sso.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.iweb.derxt.sso.dao.UserMapper;
import com.iweb.derxt.sso.data.User;
import com.iweb.derxt.sso.domain.UserDomain;
import com.iweb.derxt.sso.model.params.user.UserParam;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserDomainRepository {
    @Resource
    private UserMapper userMapper;

    public UserDomain createUserDomain(UserParam userParam) {
        return new UserDomain(this, userParam);
    }
    public User findUserByUnionId(String unionId){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUnionId, unionId);
        wrapper.last("limit 1");//sql优化 只取一个 检索到 立刻停止
        return userMapper.selectOne(wrapper);
    }

    public void saveUser(User user){
        userMapper.insert(user);
    }

    public void updateUser(User user){
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(User::getLastLoginTime, user.getLastLoginTime());
        wrapper.eq(User::getId, user.getId());
        userMapper.update(wrapper);
    }

    public User findUserById(Long userId){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId,userId);
        wrapper.last("limit 1");//sql优化 只取一个 检索到 立刻停止
        return userMapper.selectOne(wrapper);
    }
}
