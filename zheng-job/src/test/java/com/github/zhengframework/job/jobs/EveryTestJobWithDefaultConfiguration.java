package com.github.zhengframework.job.jobs;

import com.github.zhengframework.job.annotations.Every;

@Every("10ms")
public class EveryTestJobWithDefaultConfiguration extends AbstractJob {

  public EveryTestJobWithDefaultConfiguration() {
    super(1);
  }
}
