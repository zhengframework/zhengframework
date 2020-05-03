package com.github.zhengframework.job;

import com.google.inject.Injector;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

@Slf4j
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
    log.debug("jobDetail={}", jobDetail);
    return injector.getInstance(jobDetail.getJobClass());
  }
}