package com.github.zhengframework.scheduler;

import com.google.inject.Injector;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

@Singleton
final class GuiceJobFactory implements JobFactory {

  private final Injector injector;

  @Inject
  GuiceJobFactory(Injector injector) {
    this.injector = injector;
  }

  @Override
  public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
    JobDetail jobDetail = bundle.getJobDetail();
    return injector.getInstance(jobDetail.getJobClass());
  }
}