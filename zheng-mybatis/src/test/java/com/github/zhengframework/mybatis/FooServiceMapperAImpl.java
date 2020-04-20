package com.github.zhengframework.mybatis;

import com.github.zhengframework.mybatis.mapper2.UserMapperA;
import javax.inject.Inject;
import org.mybatis.guice.transactional.Transactional;

public class FooServiceMapperAImpl implements FooService {

  @Inject
  private UserMapperA userMapper;

  @Override
  @Transactional
  public User doSomeBusinessStuff(String userId) {
    return this.userMapper.getUser(userId);
  }

}