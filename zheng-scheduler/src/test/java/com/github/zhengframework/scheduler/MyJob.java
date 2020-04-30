package com.github.zhengframework.scheduler;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
@Singleton
public class MyJob implements org.quartz.Job {

  private final MyLogic logic;

  @Inject
  public MyJob(MyLogic logic) {
    this.logic = logic;
  }

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    logic.invoke();
  }
}