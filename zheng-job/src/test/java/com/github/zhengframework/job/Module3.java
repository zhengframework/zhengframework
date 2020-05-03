package com.github.zhengframework.job;

import com.github.zhengframework.job.jobs.EveryTestJobWithDefaultConfiguration;
import com.google.inject.AbstractModule;

public class Module3 extends AbstractModule {

  @Override
  protected void configure() {
    bind(EveryTestJobWithDefaultConfiguration.class).asEagerSingleton();
    bind(EveryTestJobWithDefaultConfiguration.class).asEagerSingleton();
  }
}
