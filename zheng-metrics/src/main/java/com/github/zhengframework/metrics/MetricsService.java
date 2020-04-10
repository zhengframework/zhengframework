package com.github.zhengframework.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jmx.JmxReporter;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.github.zhengframework.service.ServiceRegistry;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import java.lang.management.ManagementFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * a Service that starts Metrics JmxReporter
 */
@SuppressWarnings("UnstableApiUsage")
@Slf4j
public class MetricsService extends AbstractIdleService {

  private final MetricRegistry metricRegistry;

  private JmxReporter jmxReporter;

  @Inject
  public MetricsService(ServiceRegistry services, MetricRegistry metricRegistry) {
    this.metricRegistry = metricRegistry;

    metricRegistry.register("jvm.buffers",
        new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
    metricRegistry.register("jvm.gc", new GarbageCollectorMetricSet());
    metricRegistry.register("jvm.memory", new MemoryUsageGaugeSet());
    metricRegistry.register("jvm.threads", new ThreadStatesGaugeSet());

    services.add(this);
  }

  @Override
  protected void startUp() {
    jmxReporter = JmxReporter.forRegistry(metricRegistry).build();
    jmxReporter.start();
  }

  @Override
  protected void shutDown() {
    jmxReporter.stop();
  }
}
