package com.github.zhengframework.eventbus;

import com.google.inject.AbstractModule;

public class MyModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Pub.class);
    bind(Sub.class);
  }
}
