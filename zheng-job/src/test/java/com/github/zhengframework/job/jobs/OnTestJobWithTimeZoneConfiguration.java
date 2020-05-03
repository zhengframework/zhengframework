package com.github.zhengframework.job.jobs;

import com.github.zhengframework.job.annotations.On;

@On(value = "0 0 13 ? * MON", timeZone = "Europe/Stockholm")
public class OnTestJobWithTimeZoneConfiguration extends AbstractJob {

  public OnTestJobWithTimeZoneConfiguration() {
    super(1);
  }
}
