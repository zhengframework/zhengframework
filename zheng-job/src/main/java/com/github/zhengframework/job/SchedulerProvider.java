package com.github.zhengframework.job;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;

@Singleton
public class SchedulerProvider implements Provider<Scheduler> {

  private final GuiceJobFactory guiceJobFactory;

  private final SchedulerFactory schedulerFactory;
  private Scheduler scheduler;

  @Inject
  public SchedulerProvider(GuiceJobFactory guiceJobFactory,
      SchedulerFactory schedulerFactory) {
    this.guiceJobFactory = guiceJobFactory;

    this.schedulerFactory = schedulerFactory;
  }

  @Override
  public Scheduler get() {
    if (scheduler == null) {
      try {
        scheduler = schedulerFactory.getScheduler();
        scheduler.setJobFactory(guiceJobFactory);
      } catch (SchedulerException e) {
        throw new RuntimeException(e);
      }
    }
    return scheduler;
  }
}
