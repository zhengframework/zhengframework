package com.github.zhengframework.swagger;

import com.google.inject.servlet.ServletModule;

public class MyModule extends ServletModule {

  @Override
  protected void configureServlets() {
    bind(TestResource.class);
  }
}
