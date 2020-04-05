package com.dadazhishi.zheng.configuration.resolver;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

public class ReloadingTrigger {

  final ThreadFactory factory =
      new BasicThreadFactory.Builder()
          .namingPattern("ReloadingTrigger-%s").daemon(true)
          .build();

  private final Reloadable reloadable;
  private final long delay;
  private final TimeUnit unit;
  ScheduledExecutorService executorService;
  private ScheduledFuture<?> triggerTask;

  public ReloadingTrigger(Reloadable reloadable, long delay, TimeUnit unit) {
    this.reloadable = reloadable;
    this.delay = delay;
    this.unit = unit;
    executorService = Executors.newScheduledThreadPool(1, factory);
  }

  public void start() {
    if (!isRunning()) {
      triggerTask = executorService.scheduleWithFixedDelay(reloadable::reload, delay, delay, unit);
    }
  }

  public synchronized void stop() {
    if (isRunning()) {
      triggerTask.cancel(true);
    }

  }

  public synchronized boolean isRunning() {
    return triggerTask != null;
  }

  public void shutdown(final boolean shutdownExecutor) {
    stop();
    if (shutdownExecutor) {
      executorService.shutdown();
    }
  }

  public void shutdown() {
    shutdown(true);
  }
}
