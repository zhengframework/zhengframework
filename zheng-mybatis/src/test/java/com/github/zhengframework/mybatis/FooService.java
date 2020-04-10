package com.github.zhengframework.mybatis;

import org.mybatis.guice.transactional.Transactional;

public interface FooService {

  User doSomeBusinessStuff(String userId);

  @Transactional
  User doSomeBusinessStuff2(String userId);
}
