package com.github.zhengframework.job;

import com.github.zhengframework.job.jobs.EveryTestJobWithDefaultConfiguration;
import com.github.zhengframework.job.jobs.OnTestJobWithDefaultConfiguration;
import com.google.inject.AbstractModule;

public class Module2 extends AbstractModule {

  @Override
  protected void configure() {
    bind(EveryTestJobWithDefaultConfiguration.class).asEagerSingleton();
    bind(OnTestJobWithDefaultConfiguration.class).asEagerSingleton();
  }
}
