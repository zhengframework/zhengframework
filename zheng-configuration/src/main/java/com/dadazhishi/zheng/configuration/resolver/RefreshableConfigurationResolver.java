package com.dadazhishi.zheng.configuration.resolver;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public abstract class RefreshableConfigurationResolver implements ConfigurationResolver {

  private static final ThreadFactory threadFactory = runnable -> {
    Thread thread = Executors.defaultThreadFactory().newThread(runnable);
    thread.setDaemon(true);
    return thread;
  };

  private final AtomicReference<Map<String, String>> mapReference;

  public RefreshableConfigurationResolver() {
    this(1, 1, TimeUnit.MINUTES);
  }

  public RefreshableConfigurationResolver(long initialDelay,
      long delay,
      TimeUnit unit) {
    mapReference = new AtomicReference<>();
    doUpdate();
    ScheduledExecutorService executorService = Executors
        .newSingleThreadScheduledExecutor(threadFactory);
    executorService.scheduleWithFixedDelay(this::doUpdate, initialDelay, delay, unit);
  }

  public abstract void doUpdate();

  protected void update(Map<String, String> newValue) {
    mapReference.set(newValue);
  }

  @Override
  public Optional<String> get(String key) {
    return Optional.ofNullable(mapReference.get().get(key));
  }

  @Override
  public Set<String> keySet() {
    return mapReference.get().keySet();
  }
}
