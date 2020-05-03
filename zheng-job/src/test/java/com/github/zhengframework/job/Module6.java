package com.github.zhengframework.job;

import com.github.zhengframework.job.jobs.InjectEveryTestJob;
import com.google.inject.AbstractModule;

public class Module6 extends AbstractModule {

  @Override
  protected void configure() {
    bind(InjectEveryTestJob.class).asEagerSingleton();
  }
}
