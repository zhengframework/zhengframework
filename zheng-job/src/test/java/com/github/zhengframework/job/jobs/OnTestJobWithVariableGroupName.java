package com.github.zhengframework.job.jobs;

import com.github.zhengframework.job.annotations.On;

@On("0/1 * * * * ?")
public class OnTestJobWithVariableGroupName extends AbstractJob {

  public OnTestJobWithVariableGroupName(String groupName) {
    super(1, groupName);
  }
}
