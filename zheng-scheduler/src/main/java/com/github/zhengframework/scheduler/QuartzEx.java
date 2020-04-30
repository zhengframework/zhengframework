package com.github.zhengframework.scheduler;

import org.quartz.Scheduler;

public interface QuartzEx {

  /**
   * return Scheduler
   *
   * @return Scheduler
   */
  Scheduler getScheduler();
}