package com.github.zhengframework.core;

import com.google.inject.AbstractModule;

public class ServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    super.configure();
    bind(ServiceManager.class).asEagerSingleton();
  }


}
