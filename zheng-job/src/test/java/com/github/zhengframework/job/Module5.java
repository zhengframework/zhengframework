package com.github.zhengframework.job;

import com.github.zhengframework.job.jobs.EveryTestJobWithNonDefaultConfiguration;
import com.github.zhengframework.job.jobs.OnTestJobWithNonDefaultConfiguration;
import com.google.inject.AbstractModule;

public class Module5 extends AbstractModule {

  @Override
  protected void configure() {
    bind(EveryTestJobWithNonDefaultConfiguration.class).asEagerSingleton();
    bind(OnTestJobWithNonDefaultConfiguration.class).asEagerSingleton();
  }
}
