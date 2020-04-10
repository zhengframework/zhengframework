package com.github.zhengframework.service;

import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import javax.inject.Singleton;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("UnstableApiUsage")
@Slf4j
@EqualsAndHashCode(callSuper = false, of = {})  // makes installation of this module idempotent
public class ServicesModule extends AbstractModule {

  @Override
  protected void configure() {
  }

  @Provides
  @Singleton
  public ServiceManager serviceManager(ServiceRegistry serviceRegistry) {
    return serviceRegistry.makeServiceManager();
  }
}
