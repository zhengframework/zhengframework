package com.dadazhishi.zheng.rest;

import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import com.google.inject.servlet.ServletScopes;

public class MyModule extends ServletModule {

  @Override
  protected void configureServlets() {

    bind(HelloResource.class);
    bind(TestResource.class);
    serve("/hello1234").with(HelloServlet.class);
    bind(MySingleton.class).in(Scopes.SINGLETON);
    bind(PerRequestService.class).in(ServletScopes.REQUEST);
  }

}
