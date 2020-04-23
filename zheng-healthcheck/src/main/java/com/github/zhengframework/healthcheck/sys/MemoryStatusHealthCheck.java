package com.github.zhengframework.healthcheck.sys;

import com.github.zhengframework.healthcheck.NamedHealthCheck;

public class MemoryStatusHealthCheck extends NamedHealthCheck {

  private final long availableMemThreshold;

  public MemoryStatusHealthCheck(long availableMemThreshold) {
    this.availableMemThreshold = availableMemThreshold;
  }

  public MemoryStatusHealthCheck() {
    this(50 * 1024 * 1024);
  }

  @Override
  public String getName() {
    return "MemoryStatusHealthCheck";
  }

  @Override
  protected Result check() {
    Runtime runtime = Runtime.getRuntime();
    long freeMemory = runtime.freeMemory();
    long totalMemory = runtime.totalMemory();
    long maxMemory = runtime.maxMemory();
    boolean ok = maxMemory - (totalMemory - freeMemory) > availableMemThreshold;
    if (ok) {
      return Result.healthy();
    } else {
      String msg =
          "max:" + (maxMemory / 1024 / 1024) + "M,total:" + (totalMemory / 1024 / 1024) + "M,used:"
              + ((totalMemory / 1024 / 1024) - (freeMemory / 1024 / 1024)) + "M,free:" + (freeMemory
              / 1024 / 1024) + "M";
      return Result.unhealthy(msg);
    }
  }
}
