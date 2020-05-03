package com.github.zhengframework.job.jobs;

import com.github.zhengframework.job.annotations.Every;

@Every(value = "10ms", requestRecovery = true, storeDurably = true, priority = 20, misfirePolicy = Every.MisfirePolicy.IGNORE_MISFIRES)
public class EveryTestJobWithNonDefaultConfiguration extends AbstractJob {

  public EveryTestJobWithNonDefaultConfiguration() {
    super(1);
  }
}
