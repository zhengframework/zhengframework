package com.dadazhishi.zheng.swagger;

import com.google.inject.servlet.ServletModule;

public class MyModule extends ServletModule {

  @Override
  protected void configureServlets() {
    serve("/hello").with(HelloServlet.class);
    bind(TestResource.class);
  }
}
