package com.github.zhengframework.job.jobs;

import com.github.zhengframework.job.annotations.On;

@On(value = "0/1 * * * * ?", requestRecovery = true, storeDurably = true, priority = 20, misfirePolicy = On.MisfirePolicy.IGNORE_MISFIRES)
public class OnTestJobWithNonDefaultConfiguration extends AbstractJob {

  public OnTestJobWithNonDefaultConfiguration() {
    super(1);
  }
}
