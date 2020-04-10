package com.github.zhengframework.web;

import com.google.inject.servlet.ServletModule;

public class MyModule extends ServletModule {

  @Override
  protected void configureServlets() {
    serve("/hello").with(HelloServlet.class);
  }
}
