package com.dadazhishi.zheng.rest;

import com.dadazhishi.zheng.web.WebConfig;
import com.google.inject.Provides;
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

  @Provides
  WebConfig webConfig() {
    return new WebConfig();
  }
}
