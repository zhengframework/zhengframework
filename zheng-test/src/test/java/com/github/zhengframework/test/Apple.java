package com.github.zhengframework.test;

import com.google.inject.AbstractModule;

public interface Apple {

  class Module extends AbstractModule {

    @Override
    protected void configure() {
      bind(Apple.class).to(AppleImpl.class);
    }
  }

}
