package com.github.zhengframework.test;

import com.google.inject.AbstractModule;

public interface User {

  class Module extends AbstractModule {

    @Override
    protected void configure() {
      bind(User.class).to(UserImpl.class);
    }
  }
}
