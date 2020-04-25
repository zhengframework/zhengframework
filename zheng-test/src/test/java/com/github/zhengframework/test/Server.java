package com.github.zhengframework.test;

import com.google.inject.AbstractModule;

public interface Server {

  class Module extends AbstractModule {

    @Override
    protected void configure() {
      bind(Server.class).to(ServerImpl.class);
    }
  }
}
