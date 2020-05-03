package com.github.zhengframework.job.jobs;

import com.github.zhengframework.job.annotations.On;

@On("0/1 * * * * ?")
public class OnTestJobWithDefaultConfiguration extends AbstractJob {

  public OnTestJobWithDefaultConfiguration() {
    super(1);
  }
}
