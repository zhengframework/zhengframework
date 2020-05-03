package com.github.zhengframework.job.jobs;


import com.github.zhengframework.job.annotations.Every;
import com.google.inject.Inject;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Every("10ms")
public class InjectEveryTestJob extends AbstractJob {

  private Dependency dependency;

  @Inject
  public InjectEveryTestJob(Dependency dependency) {
    super(1);
    this.dependency = dependency;
  }

  @Override
  public void doJob(JobExecutionContext context) throws JobExecutionException {
    if (dependency == null) {
      throw new IllegalStateException("dependency is null");
    }
    super.doJob(context);

  }
}
