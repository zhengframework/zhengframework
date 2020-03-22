package com.dadazhishi.zheng.jersey;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.servlet.ServletScopes;

public class MyModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(MySingleton.class).in(Scopes.SINGLETON);
    bind(PerRequestService.class).in(ServletScopes.REQUEST);
  }
}
