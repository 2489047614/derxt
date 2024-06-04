package com.iweb.derxt.sso.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iweb.derxt.sso.data.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
