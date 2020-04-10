package com.github.zhengframework.mybatis;

import javax.inject.Inject;
import org.mybatis.guice.transactional.Transactional;

public class FooServiceMapperImpl implements FooService {

  @Inject
  private UserMapper userMapper;

  @Inject
  private UserMapper2 userMapper2;

  @Override
  @Transactional
  public User doSomeBusinessStuff(String userId) {
    return this.userMapper.getUser(userId);
  }

  @Override
  @Transactional
  public User doSomeBusinessStuff2(String userId) {
    return this.userMapper2.getUser(userId);
  }

}