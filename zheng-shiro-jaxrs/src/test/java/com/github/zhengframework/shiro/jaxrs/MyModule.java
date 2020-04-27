package com.github.zhengframework.shiro.jaxrs;

import com.google.inject.AbstractModule;

public class MyModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(TestResource.class);
  }
}
