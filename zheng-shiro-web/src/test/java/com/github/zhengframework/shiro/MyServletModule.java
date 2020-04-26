package com.github.zhengframework.shiro;

import com.google.inject.servlet.ServletModule;
import javax.inject.Singleton;

public class MyServletModule extends ServletModule {

  @Override
  protected void configureServlets() {
    bind(LoginServlet.class).in(Singleton.class);
    serve("/login").with(LoginServlet.class);
  }
}
