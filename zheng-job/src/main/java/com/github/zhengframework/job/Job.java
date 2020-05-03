package com.github.zhengframework.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public abstract class Job implements org.quartz.Job {

  private final String groupName;

  public Job(String groupName) {
    this.groupName = groupName;
  }

  public Job() {
    groupName = null;
  }

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    doJob(context);
  }

  public abstract void doJob(JobExecutionContext context) throws JobExecutionException;

  public String getGroupName() {
    return groupName;
  }
}
