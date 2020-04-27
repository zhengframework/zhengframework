package com.github.zhengframework.metrics;

import com.google.inject.AbstractModule;

public class MyModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(TestService.class);
  }

}
