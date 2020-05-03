package com.github.zhengframework.job;

import com.github.zhengframework.job.jobs.OnTestJobWithDefaultConfiguration;
import com.github.zhengframework.job.jobs.OnTestJobWithTimeZoneConfiguration;
import com.google.inject.AbstractModule;

public class Module4 extends AbstractModule {

  @Override
  protected void configure() {
    bind(OnTestJobWithTimeZoneConfiguration.class).asEagerSingleton();
    bind(OnTestJobWithDefaultConfiguration.class).asEagerSingleton();
  }
}
