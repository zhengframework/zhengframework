package com.github.zhengframework.scheduler;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;

@Singleton
final class QuartzExImpl implements QuartzEx {

  private final Scheduler scheduler;

  @Inject
  QuartzExImpl(SchedulerFactory schedulerFactory, GuiceJobFactory jobFactory)
      throws SchedulerException {
    this.scheduler = schedulerFactory.getScheduler();
    this.scheduler.setJobFactory(jobFactory);
    this.scheduler.start();
  }

  /**
   * return Scheduler
   *
   * @return Scheduler
   */
  @Override
  public final Scheduler getScheduler() {
    return this.scheduler;
  }
}
