package com.github.zhengframework.job;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.github.zhengframework.job.annotations.Every;
import com.github.zhengframework.job.annotations.On;
import com.github.zhengframework.job.jobs.ApplicationStartTestJob;
import com.github.zhengframework.job.jobs.ApplicationStopTestJob;
import com.github.zhengframework.job.jobs.EveryTestJob;
import com.github.zhengframework.job.jobs.EveryTestJobWithDefaultConfiguration;
import com.github.zhengframework.job.jobs.EveryTestJobWithDelay;
import com.github.zhengframework.job.jobs.EveryTestJobWithJobName;
import com.github.zhengframework.job.jobs.EveryTestJobWithNonDefaultConfiguration;
import com.github.zhengframework.job.jobs.EveryTestJobWithSameJobName;
import com.github.zhengframework.job.jobs.InjectEveryTestJob;
import com.github.zhengframework.job.jobs.OnTestJob;
import com.github.zhengframework.job.jobs.OnTestJobWithDefaultConfiguration;
import com.github.zhengframework.job.jobs.OnTestJobWithJobName;
import com.github.zhengframework.job.jobs.OnTestJobWithNonDefaultConfiguration;
import com.github.zhengframework.job.jobs.OnTestJobWithTimeZoneConfiguration;
import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import com.google.inject.Inject;
import com.google.inject.Injector;
import java.util.concurrent.TimeUnit;
import org.hamcrest.core.IsEqual;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;

@RunWith(ZhengApplicationRunner.class)
public class JobModuleTest {

  public static ApplicationStartTestJob startTestJob = new ApplicationStartTestJob();
  public static OnTestJob onTestJob = new OnTestJob();
  public static OnTestJobWithJobName onTestJobWithJobName = new OnTestJobWithJobName();
  public static EveryTestJob everyTestJob = new EveryTestJob();
  public static EveryTestJobWithDelay everyTestJobWithDelay = new EveryTestJobWithDelay();
  public static EveryTestJobWithJobName everyTestJobWithJobName = new EveryTestJobWithJobName();
  public static ApplicationStopTestJob applicationStopTestJob = new ApplicationStopTestJob();
  @Inject private Scheduler scheduler;
  @Inject private Injector injector;

  @WithZhengApplication(moduleClass = Module1.class)
  @Test
  public void testThatJobsAreExecuted() throws Exception {
    // a job with an @Every annotation that doesn't specify a job name should be assigned the
    // canonical class name
    String jobName = EveryTestJob.class.getCanonicalName();
    assertTrue(scheduler.checkExists(JobKey.jobKey(jobName)));

    // a job with an @Every annotation that specifies a job name should get that name, not the
    // canonical class name
    jobName = EveryTestJobWithJobName.class.getAnnotation(Every.class).jobName();
    assertTrue(scheduler.checkExists(JobKey.jobKey(jobName)));

    // a job with an @On annotation that doesn't specify a job name should be assigned the canonical
    // class name
    jobName = OnTestJob.class.getCanonicalName();
    assertTrue(scheduler.checkExists(JobKey.jobKey(jobName)));

    // a job with an @On annotation that specifies a job name should get that name, not the
    // canonical class name
    jobName = OnTestJobWithJobName.class.getAnnotation(On.class).jobName();
    assertTrue(scheduler.checkExists(JobKey.jobKey(jobName)));

    // if there are two jobs that have the same name, only one job and trigger will be created with
    // that job name
    // this simulates running in clustered environments where two or more nodes have the same set of
    // jobs
    jobName = EveryTestJobWithJobName.class.getAnnotation(Every.class).jobName();
    assertThat(
        jobName,
        IsEqual.equalTo(EveryTestJobWithSameJobName.class.getAnnotation(Every.class).jobName()));
    assertTrue(scheduler.checkExists(JobKey.jobKey(jobName)));
    assertThat(scheduler.getTriggersOfJob(JobKey.jobKey(jobName)).size(), IsEqual.equalTo(1));

    assertThat(everyTestJobWithDelay.latch().await(100, TimeUnit.MILLISECONDS), is(false));
    assertThat(everyTestJobWithJobName.latch().await(1, TimeUnit.SECONDS), is(true));
    assertThat(everyTestJob.latch().await(1, TimeUnit.SECONDS), is(true));

    assertThat(everyTestJobWithDelay.latch().await(2, TimeUnit.SECONDS), is(true));
    assertThat(onTestJob.latch().await(2, TimeUnit.SECONDS), is(true));

    JobManagerService jobManagerService = injector.getInstance(JobManagerService.class);
    jobManagerService.stop();
    assertThat(applicationStopTestJob.latch().await(2, TimeUnit.SECONDS), is(true));
  }

  @WithZhengApplication(moduleClass = Module2.class)
  @Test
  public void testJobsWithDefaultConfiguration() throws Exception {
    String jobName = EveryTestJobWithDefaultConfiguration.class.getCanonicalName();
    JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName));
    Trigger trigger = scheduler.getTriggersOfJob(JobKey.jobKey(jobName)).get(0);

    assertFalse(jobDetail.requestsRecovery());
    assertFalse(jobDetail.isDurable());
    assertEquals(Trigger.DEFAULT_PRIORITY, trigger.getPriority());
    assertEquals(Trigger.MISFIRE_INSTRUCTION_SMART_POLICY, trigger.getMisfireInstruction());

    jobName = OnTestJobWithDefaultConfiguration.class.getCanonicalName();
    jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName));
    trigger = scheduler.getTriggersOfJob(JobKey.jobKey(jobName)).get(0);

    assertFalse(jobDetail.requestsRecovery());
    assertFalse(jobDetail.isDurable());
    assertEquals(Trigger.DEFAULT_PRIORITY, trigger.getPriority());
    assertEquals(Trigger.MISFIRE_INSTRUCTION_SMART_POLICY, trigger.getMisfireInstruction());
  }

  @WithZhengApplication(moduleClass = Module3.class)
  @Test
  public void testJobsWithoutGroupShouldOnlyHaveOneInstance() throws Exception {
    String jobName = EveryTestJobWithDefaultConfiguration.class.getCanonicalName();
    JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName));
    Trigger trigger = scheduler.getTriggersOfJob(JobKey.jobKey(jobName)).get(0);

    assertNotNull(jobDetail);
    assertNotNull(trigger);
    assertThat(scheduler.getJobKeys(GroupMatcher.anyGroup()).size(), equalTo(1));
  }

  @WithZhengApplication(moduleClass = Module4.class)
  @Test
  public void testJobsWithTimeZoneInOnAnnotation() throws Exception {
    String jobName = OnTestJobWithTimeZoneConfiguration.class.getCanonicalName();
    CronTrigger trigger = (CronTrigger) scheduler.getTriggersOfJob(JobKey.jobKey(jobName)).get(0);
    assertEquals("Europe/Stockholm", trigger.getTimeZone().getID());
  }

  @WithZhengApplication(moduleClass = Module5.class)
  @Test
  public void testJobsWithNonDefaultConfiguration() throws Exception {

    String jobName = EveryTestJobWithNonDefaultConfiguration.class.getCanonicalName();
    JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName));
    Trigger trigger = scheduler.getTriggersOfJob(JobKey.jobKey(jobName)).get(0);

    assertTrue(jobDetail.requestsRecovery());
    assertTrue(jobDetail.isDurable());
    assertEquals(20, trigger.getPriority());
    assertEquals(
        Trigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY, trigger.getMisfireInstruction());

    jobName = OnTestJobWithNonDefaultConfiguration.class.getCanonicalName();
    jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName));
    trigger = scheduler.getTriggersOfJob(JobKey.jobKey(jobName)).get(0);

    assertTrue(jobDetail.requestsRecovery());
    assertTrue(jobDetail.isDurable());
    assertEquals(20, trigger.getPriority());
    assertEquals(
        Trigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY, trigger.getMisfireInstruction());
  }

  @WithZhengApplication(moduleClass = Module6.class)
  @Test
  public void testInjectJob() throws SchedulerException, InterruptedException {
    InjectEveryTestJob everyTestJob = injector.getInstance(InjectEveryTestJob.class);
    String jobName = InjectEveryTestJob.class.getCanonicalName();
    assertTrue(scheduler.checkExists(JobKey.jobKey(jobName)));
    assertThat(everyTestJob.latch().await(100, TimeUnit.MILLISECONDS), is(true));
  }
}
