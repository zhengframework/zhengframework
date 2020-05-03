package com.github.zhengframework.job;

import com.github.zhengframework.job.jobs.ApplicationStartTestJob;
import com.github.zhengframework.job.jobs.ApplicationStopTestJob;
import com.github.zhengframework.job.jobs.EveryTestJob;
import com.github.zhengframework.job.jobs.EveryTestJobWithDelay;
import com.github.zhengframework.job.jobs.EveryTestJobWithJobName;
import com.github.zhengframework.job.jobs.OnTestJob;
import com.github.zhengframework.job.jobs.OnTestJobWithJobName;
import com.google.inject.AbstractModule;

public class Module1 extends AbstractModule {

  @Override
  protected void configure() {
    bind(ApplicationStartTestJob.class).toInstance(JobModuleTest.startTestJob);
    bind(OnTestJob.class).toInstance(JobModuleTest.onTestJob);
    bind(OnTestJobWithJobName.class).toInstance(JobModuleTest.onTestJobWithJobName);
    bind(EveryTestJob.class).toInstance(JobModuleTest.everyTestJob);
    bind(EveryTestJobWithDelay.class).toInstance(JobModuleTest.everyTestJobWithDelay);
    bind(EveryTestJobWithJobName.class).toInstance(JobModuleTest.everyTestJobWithJobName);
    bind(ApplicationStopTestJob.class).toInstance(JobModuleTest.applicationStopTestJob);
  }
}
