code base on [dropwizard-jobs](https://github.com/dropwizard-jobs/dropwizard-jobs)

There are four different types of jobs:

* Jobs run on application start for initial setup
* Jobs run at application stop before the application is closed
* Jobs which are repeated after a certain time interval
* Jobs which need to run at a specific time, via a cron-like expression

## Available job types

The <code>@OnApplicationStart</code> annotation triggers a job after the quartz scheduler is started

```java
@OnApplicationStart
public class StartupJob extends AbstractJob {
  @Override
  public void doJob(JobExecutionContext context) throws JobExecutionException {
    // logic run once on application start
  }
}
```

The <code>@OnApplicationStop</code> annotation triggers a job when the application is stopped. Be aware that it is not guaranteed that this job is executed, in case the application is killed.

```java
@OnApplicationStop
public class StopJob extends AbstractJob {
  @Override
  public void doJob(JobExecutionContext context) throws JobExecutionException {
    // logic run once on application stop
  }
}
```

The <code>@Every</code> annotation first triggers a job after the quartz scheduler is started and then every n times, as it is configured. You can use a number and a time unit, which can be one of "s" for seconds, "mn" or "min" for minutes, "h" for hours and "d" for days.
Use in conjunction with <code>@DelayStart</code> to delay the first invocation of this job.

```java
@Every("1s")
public class EveryTestJob extends AbstractJob {
  @Override
  public void doJob(JobExecutionContext context) throws JobExecutionException {
    // logic run every time and time again
  }
}
```

The <code>@DelayStart</code> annotation can be used in conjunction with @Every to delay the start of the job. Without this, all the @Every jobs start up at the same time when the scheduler starts.

```java
@DelayStart("5s")
@Every("1s")
public class EveryTestJobWithDelayedStart extends AbstractJob {
  @Override
  public void doJob(JobExecutionContext context) throws JobExecutionException {
    // logic run every time and time again
  }
}
```

The <code>@On</code> annotation allows one to use cron-like expressions for complex time settings. You can read more about possible cron expressions at http://quartz-scheduler.org/documentation/quartz-2.1.x/tutorials/tutorial-lesson-06

This expression would run on Mondays at 1pm, Los Angeles time. If the optional parameter `timeZone` is not set system default will be used. 

```java
@On("0 0 13 ? * MON", timeZone = "America/Los_Angeles")
public class OnTestJob extends AbstractJob {
  @Override
  public void doJob(JobExecutionContext context) throws JobExecutionException {
    // logic run via cron expression
  }
}
```

all define job should bind asEagerSingleton and direct class.
```java
public class MyJobModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(MyJob1.class).asEagerSingleton();// work fine
    bind(MyJob2.class).asEagerSingleton();// work fine
    bind(AbstractJob.class).to(MyJob3.class).asEagerSingleton();// not working
  }
}
```

