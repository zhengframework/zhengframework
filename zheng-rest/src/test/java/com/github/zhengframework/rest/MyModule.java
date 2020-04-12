package com.github.zhengframework.rest;

import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import com.google.inject.servlet.ServletScopes;

public class MyModule extends ServletModule {

  @Override
  protected void configureServlets() {

    bind(TestResource.class);
    bind(MySingleton.class).in(Scopes.SINGLETON);
    bind(PerRequestService.class).in(ServletScopes.REQUEST);
  }

}
