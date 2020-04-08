package com.dadazhishi.zheng.configuration.reload;

import static java.util.Objects.requireNonNull;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

public class MeteredReloadable implements Reloadable {

  private final Reloadable delegate;
  private final Timer reloadTimer;

  public MeteredReloadable(MetricRegistry metricRegistry, String metricPrefix,
      Reloadable delegate) {
    requireNonNull(metricRegistry);
    requireNonNull(metricPrefix);
    this.delegate = requireNonNull(delegate);

    reloadTimer = metricRegistry.timer(metricPrefix + "reloadable.reload");
  }

  @Override
  public void reload() {
    Timer.Context context = reloadTimer.time();
    try {
      delegate.reload();
    } finally {
      context.stop();
    }
  }
}
