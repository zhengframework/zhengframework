package com.github.zhengframework.healthcheck.sys;

import com.github.zhengframework.healthcheck.NamedHealthCheck;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class SystemLoadHealthCheck extends NamedHealthCheck {

  @Override
  public String getName() {
    return "SystemLoadHealthCheck";
  }

  @Override
  protected Result check() throws Exception {
    OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
    double load = operatingSystemMXBean.getSystemLoadAverage();
    int cpu = operatingSystemMXBean.getAvailableProcessors();
    if (load < cpu) {
      return Result.healthy();
    } else {
      return Result.unhealthy("load:%s,cpu:%s", load, cpu);
    }
  }
}
