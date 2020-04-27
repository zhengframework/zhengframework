package com.github.zhengframework.service;

import com.google.inject.AbstractModule;

public class ServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    super.configure();
    bind(ServiceManager.class).asEagerSingleton();
  }


}
