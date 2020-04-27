package com.github.zhengframework.service;

import com.google.inject.AbstractModule;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceShutdownHookModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(SystemShutdownHook.class).asEagerSingleton();
  }

  @Singleton
  public static class SystemShutdownHook extends Thread {

    @Inject
    public SystemShutdownHook(final ServiceManager serviceManager) {
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        try {
          log.info("Runtime Lifecycle shutdown hook begin running");
          serviceManager.stop();
        } catch (Throwable t) {
          log.info("Runtime Lifecycle shutdown hook result in error ", t);
          throw t;
        }
        log.info("Runtime Lifecycle shutdown hook finished running");
      }));
    }
  }
}
