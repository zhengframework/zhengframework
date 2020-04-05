package com.dadazhishi.zheng.rest;

import com.google.inject.servlet.ServletModule;
import javax.inject.Singleton;

public class MyModule extends ServletModule {

  @Override
  protected void configureServlets() {
    bind(HelloResource.class);
    bind(TestResource.class);
    bind(HelloServlet.class).in(Singleton.class);
    serve("/hello1234").with(HelloServlet.class);
  }

}
