package com.github.zhengframework.mybatis;

import com.github.zhengframework.mybatis.mapper2.UserMapper2;
import javax.inject.Inject;
import org.mybatis.guice.transactional.Transactional;

public class FooServiceMapper2Impl implements FooService {

  @Inject
  private UserMapper2 userMapper2;

  @Override
  @Transactional
  public User doSomeBusinessStuff(String userId) {
    return this.userMapper2.getUser(userId);
  }

}