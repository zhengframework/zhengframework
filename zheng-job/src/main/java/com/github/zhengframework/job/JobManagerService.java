package com.github.zhengframework.job;

import com.github.zhengframework.guice.ClassScanner;
import com.github.zhengframework.job.annotations.DelayStart;
import com.github.zhengframework.job.annotations.Every;
import com.github.zhengframework.job.annotations.On;
import com.github.zhengframework.job.annotations.OnApplicationStart;
import com.github.zhengframework.job.annotations.OnApplicationStop;
import com.github.zhengframework.service.Service;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerConfigException;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

@Slf4j
public class JobManagerService implements Service {

  private final Scheduler scheduler;
  private final Provider<Injector> injectorProvider;
  protected Job[] jobs;
  protected ZoneId defaultZoneId;
  private GuiceJobFactory guiceJobFactory;
  private volatile boolean stop = false;


  @Inject
  public JobManagerService(JobConfig jobConfig, Scheduler scheduler,
      GuiceJobFactory guiceJobFactory,
      Provider<Injector> injectorProvider) {
    this.scheduler = scheduler;
    this.guiceJobFactory = guiceJobFactory;
    this.injectorProvider = injectorProvider;
    if (jobConfig.getDefaultTimezone() != null) {
      defaultZoneId = ZoneOffset.of(jobConfig.getDefaultTimezone());
    } else {
      defaultZoneId = ZoneOffset.systemDefault();
    }

    List<Job> jobs = new ArrayList<>();
    ClassScanner<Job> jobClassScanner = new ClassScanner<>(injectorProvider.get(), Job.class);
    jobClassScanner.accept(jobs::add);
    this.jobs = jobs.toArray(new Job[]{});
  }

  @Override
  public int order() {
    return 0;
  }

  @Override
  public void start() throws Exception {
    scheduler.setJobFactory(guiceJobFactory);
    scheduler.start();
    scheduleAllJobs();
    logAllOnApplicationStopJobs();
  }

  protected void scheduleAllJobs() throws SchedulerException {
    scheduleAllJobsOnApplicationStart();
    scheduleAllJobsWithEveryAnnotation();
    scheduleAllJobsWithOnAnnotation();
  }

  protected void scheduleAllJobsOnApplicationStop() throws SchedulerException {
    List<JobDetail> jobDetails = Arrays.stream(jobs)
        .filter(job -> job.getClass().isAnnotationPresent(OnApplicationStop.class))
        .map(job -> JobBuilder
            .newJob(job.getClass())
            .withIdentity(job.getClass().getName(), job.getGroupName())
            .build())
        .collect(Collectors.toList());
    for (JobDetail jobDetail : jobDetails) {
      scheduler.scheduleJob(jobDetail, executeNowTrigger());
    }
  }

  /**
   * Allow timezone to be configured on a per-cron basis with [timezoneName] appended to the cron
   * format
   *
   * @param cronExpr the modified cron format
   * @return the cron schedule with the timezone applied to it if needed
   */
  protected CronScheduleBuilder createCronScheduleBuilder(String cronExpr) {
    int i = cronExpr.indexOf("[");
    int j = cronExpr.indexOf("]");
    ZoneId zoneId = defaultZoneId;

    if (i > -1 && j > -1) {
      zoneId = ZoneOffset.of(cronExpr.substring(i + 1, j));
      cronExpr = cronExpr.substring(0, i).trim();
    }
    return CronScheduleBuilder.cronSchedule(cronExpr)
        .inTimeZone(TimeZone.getTimeZone(zoneId));
  }

  protected void scheduleAllJobsWithOnAnnotation() throws SchedulerException {
    List<Job> onJobs = Arrays.stream(this.jobs)
        .filter(job -> job.getClass().isAnnotationPresent(On.class))
        .collect(Collectors.toList());

    if (onJobs.isEmpty()) {
      return;
    }

    log.info("Jobs with @On annotation:");
    for (Job job : onJobs) {
      Class<? extends Job> clazz = job.getClass();
      On onAnnotation = clazz.getAnnotation(On.class);
      String cron = onAnnotation.value();

      if (cron.isEmpty()) {
        throw new SchedulerConfigException("Missing cron value for " + clazz.getSimpleName());
      }

      int priority = onAnnotation.priority();
      On.MisfirePolicy misfirePolicy = onAnnotation.misfirePolicy();
      boolean requestRecovery = onAnnotation.requestRecovery();
      boolean storeDurably = onAnnotation.storeDurably();

      CronScheduleBuilder scheduleBuilder = createCronScheduleBuilder(cron);

      String timeZoneStr = onAnnotation.timeZone();
      if (StringUtils.isNotBlank(timeZoneStr)) {
        TimeZone timeZone = TimeZone.getTimeZone(ZoneId.of(timeZoneStr));
        scheduleBuilder = scheduleBuilder.inTimeZone(timeZone);
      }

      if (misfirePolicy == On.MisfirePolicy.IGNORE_MISFIRES) {
        scheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
      } else if (misfirePolicy == On.MisfirePolicy.DO_NOTHING) {
        scheduleBuilder.withMisfireHandlingInstructionDoNothing();
      } else if (misfirePolicy == On.MisfirePolicy.FIRE_AND_PROCEED) {
        scheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
      }

      Trigger trigger = TriggerBuilder.newTrigger().withSchedule(scheduleBuilder)
          .withPriority(priority).build();

      // ensure that only one instance of each job is scheduled
      JobKey jobKey = createJobKey(onAnnotation.jobName(), job);

      createOrUpdateJob(jobKey, clazz, trigger, requestRecovery, storeDurably);
      log.info(String.format("    %-21s %s", cron, jobKey.toString()));
    }
  }

  private JobKey createJobKey(final String annotationJobName, final Job job) {
    String key = StringUtils.isNotBlank(annotationJobName) ? annotationJobName
        : job.getClass().getCanonicalName();
    return JobKey.jobKey(key, job.getGroupName());
  }

  protected void scheduleAllJobsWithEveryAnnotation() throws SchedulerException {
    List<Job> everyJobs = Arrays.stream(this.jobs)
        .filter(job -> job.getClass().isAnnotationPresent(Every.class))
        .collect(Collectors.toList());

    if (everyJobs.isEmpty()) {
      return;
    }

    log.info("Jobs with @Every annotation:");
    for (Job job : everyJobs) {
      Class<? extends Job> clazz = job.getClass();
      Every everyAnnotation = clazz.getAnnotation(Every.class);
      int priority = everyAnnotation.priority();
      Every.MisfirePolicy misfirePolicy = everyAnnotation.misfirePolicy();
      boolean requestRecovery = everyAnnotation.requestRecovery();
      boolean storeDurably = everyAnnotation.storeDurably();
      int repeatCount = everyAnnotation.repeatCount();

      String value = everyAnnotation.value();
      if (value.isEmpty()) {
        throw new SchedulerConfigException("Missing every value for " + clazz.getSimpleName());
      }
      long milliSeconds = TimeParserUtil.parseDuration(value);

      SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
          .withIntervalInMilliseconds(milliSeconds);

      if (repeatCount > -1) {
        scheduleBuilder.withRepeatCount(repeatCount);
      } else {
        scheduleBuilder.repeatForever();
      }

      if (misfirePolicy == Every.MisfirePolicy.IGNORE_MISFIRES) {
        scheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
      } else if (misfirePolicy == Every.MisfirePolicy.FIRE_NOW) {
        scheduleBuilder.withMisfireHandlingInstructionFireNow();
      } else if (misfirePolicy == Every.MisfirePolicy.NOW_WITH_EXISTING_COUNT) {
        scheduleBuilder.withMisfireHandlingInstructionNowWithExistingCount();
      } else if (misfirePolicy == Every.MisfirePolicy.NOW_WITH_REMAINING_COUNT) {
        scheduleBuilder.withMisfireHandlingInstructionNowWithRemainingCount();
      } else if (misfirePolicy == Every.MisfirePolicy.NEXT_WITH_EXISTING_COUNT) {
        scheduleBuilder.withMisfireHandlingInstructionNextWithExistingCount();
      } else if (misfirePolicy == Every.MisfirePolicy.NEXT_WITH_REMAINING_COUNT) {
        scheduleBuilder.withMisfireHandlingInstructionNextWithRemainingCount();
      }

      LocalDateTime start = LocalDateTime.now();
      DelayStart delayAnnotation = clazz.getAnnotation(DelayStart.class);
      if (delayAnnotation != null) {
        long milliSecondDelay = TimeParserUtil.parseDuration(delayAnnotation.value());
        start = start.plus(Duration.ofMillis(milliSecondDelay));
      }

      Trigger trigger = TriggerBuilder.newTrigger().withSchedule(scheduleBuilder)
          .startAt(Date.from(start.atZone(ZoneId.systemDefault()).toInstant()))
          .withPriority(priority)
          .build();

      // ensure that only one instance of each job is scheduled
      JobKey jobKey = createJobKey(everyAnnotation.jobName(), job);
      createOrUpdateJob(jobKey, clazz, trigger, requestRecovery, storeDurably);

      String logMessage = String.format("    %-7s %s", everyAnnotation.value(), jobKey.toString());
      if (delayAnnotation != null) {
        logMessage += " (" + delayAnnotation.value() + " delay)";
      }
      log.info(logMessage);
    }
  }

  protected void scheduleAllJobsOnApplicationStart() throws SchedulerException {
    List<JobDetail> jobDetails = Arrays.stream(this.jobs)
        .filter(job -> job.getClass().isAnnotationPresent(OnApplicationStart.class))
        .map(job -> JobBuilder
            .newJob(job.getClass())
            .withIdentity(job.getClass().getName(), job.getGroupName())
            .build())
        .collect(Collectors.toList());

    if (!jobDetails.isEmpty()) {
      log.info("Jobs to run on application start:");
      for (JobDetail jobDetail : jobDetails) {
        scheduler.scheduleJob(jobDetail, executeNowTrigger());
        log.info("   " + jobDetail.getJobClass().getCanonicalName());
      }
    }
  }

  protected Trigger executeNowTrigger() {
    return TriggerBuilder.newTrigger().startNow().build();
  }

  private void logAllOnApplicationStopJobs() {
    log.info("Jobs to run on application stop:");

    Arrays.stream(this.jobs)
        .filter(job -> job.getClass().isAnnotationPresent(OnApplicationStop.class))
        .map(Job::getClass)
        .forEach(clazz -> log.info("   " + clazz.getCanonicalName()));
  }

  private void createOrUpdateJob(JobKey jobKey, Class<? extends org.quartz.Job> clazz,
      Trigger trigger, boolean requestsRecovery,
      boolean storeDurably) throws SchedulerException {
    JobBuilder jobBuilder = JobBuilder.newJob(clazz).withIdentity(jobKey)
        .requestRecovery(requestsRecovery)
        .storeDurably(storeDurably);

    if (!scheduler.checkExists(jobKey)) {
      // if the job doesn't already exist, we can create it, along with its trigger. this prevents us
      // from creating multiple instances of the same job when running in a clustered environment
      scheduler.scheduleJob(jobBuilder.build(), trigger);
      log.info("scheduled job with key " + jobKey.toString());
    } else {
      // if the job has exactly one trigger, we can just reschedule it, which allows us to update the schedule for
      // that trigger.
      List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
      if (triggers.size() == 1) {
        scheduler.rescheduleJob(triggers.get(0).getKey(), trigger);
      } else {
        // if for some reason the job has multiple triggers, it's easiest to just delete and re-create the job,
        // since we want to enforce a one-to-one relationship between jobs and triggers
        scheduler.deleteJob(jobKey);
        scheduler.scheduleJob(jobBuilder.build(), trigger);
      }
    }
  }

  @Override
  public void stop() throws Exception {
    if (!stop) {
      scheduleAllJobsOnApplicationStop();

      // this is enough to put the job into the queue, otherwise the jobs wont
      // be executed, anyone got a better solution?
      Thread.sleep(100);
      scheduler.shutdown(true);
      stop = true;
    }

  }
}
