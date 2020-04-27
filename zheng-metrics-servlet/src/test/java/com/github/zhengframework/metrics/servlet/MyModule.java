package com.github.zhengframework.metrics.servlet;

import com.google.inject.AbstractModule;

public class MyModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(OneHealthCheck.class);
    bind(TestService.class);
  }
}
