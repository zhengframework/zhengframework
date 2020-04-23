package com.github.zhengframework.mybatis;

import com.github.zhengframework.mybatis.mapper.UserMapper;
import javax.inject.Inject;
import org.mybatis.guice.transactional.Transactional;

public class FooServiceMapperImpl implements FooService {

  @Inject
  private UserMapper userMapper;


  @Override
  @Transactional
  public User doSomeBusinessStuff(String userId) {
    return this.userMapper.getUser(userId);
  }

}