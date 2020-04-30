package com.github.zhengframework.scheduler;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

@Singleton
public class TrialQuartzImpl implements TrialQuartz {

  private final QuartzEx quartz;

  @Inject
  public TrialQuartzImpl(QuartzEx quartz) {
    this.quartz = quartz;
  }

  @Override
  public void run() {
    JobDetail job1 = JobBuilder.newJob(MyJob.class)
        .withIdentity("job1", "group1")
        .build();

    Trigger trigger1 = TriggerBuilder.newTrigger()
        .withIdentity("trigger1", "group1")
        .startNow()
        .withSchedule(simpleSchedule()
            .withIntervalInMilliseconds(100)
            .repeatForever())
        .build();

    Scheduler scheduler = quartz.getScheduler();
    try {
      scheduler.start();
      scheduler.scheduleJob(job1, trigger1);
    } catch (SchedulerException e) {
      throw new RuntimeException(e);
    }

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    try {
      scheduler.shutdown();
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }
}
