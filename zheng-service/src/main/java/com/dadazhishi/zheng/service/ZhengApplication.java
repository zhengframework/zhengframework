package com.dadazhishi.zheng.service;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationBuilder;
import com.dadazhishi.zheng.configuration.ConfigurationSupport;
import com.google.common.base.Preconditions;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import java.net.URL;

public class ZhengApplication {

  private Injector injector;

  public ZhengApplication(Configuration configuration, Module... modules) {
    Preconditions.checkState(configuration != null, "configuration is null");
    for (Module module : modules) {
      if (module instanceof ConfigurationSupport) {
        ConfigurationSupport configurationSupport = (ConfigurationSupport) module;
        configurationSupport.setConfiguration(configuration);
      }
    }
    injector = Guice.createInjector(modules);
  }

  public static ZhengApplication create(Configuration configuration, Module... modules) {
    return new ZhengApplication(configuration, modules);
  }

  public static ZhengApplication create(Module... modules) {
    ConfigurationBuilder configurationBuilder = ConfigurationBuilder.create();
    URL resource = ZhengApplication.class.getResource("/application.properties");
    if (resource != null) {
      configurationBuilder.withClassPathProperties("/application.properties");
    }
    Configuration configuration = configurationBuilder
        .withSystemProperties()
        .withEnvironmentVariables()
        .build();
    return new ZhengApplication(configuration, modules);
  }

  public Injector getInjector() {
    return injector;
  }

  public void start() {
    injector.getInstance(Run.class).start();
  }

  public void stop() {
    injector.getInstance(Run.class).stop();
  }

}
