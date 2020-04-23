package com.github.zhengframework.mybatis;

import com.github.zhengframework.mybatis.mapper2.UserMapperB;
import javax.inject.Inject;
import org.mybatis.guice.transactional.Transactional;

public class FooServiceMapperBImpl implements FooService {

  @Inject
  private UserMapperB userMapper;

  @Override
  @Transactional
  public User doSomeBusinessStuff(String userId) {
    return this.userMapper.getUser(userId);
  }

}