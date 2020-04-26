package com.github.zhengframework.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jmx.JmxReporter;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.github.zhengframework.service.Service;
import com.google.inject.Inject;
import java.lang.management.ManagementFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * a Service that starts Metrics JmxReporter
 */
@Slf4j
public class MetricsService implements Service {

  private final MetricRegistry metricRegistry;

  private JmxReporter jmxReporter;

  @Inject
  public MetricsService(MetricRegistry metricRegistry) {
    this.metricRegistry = metricRegistry;

    metricRegistry.register("jvm.buffers",
        new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
    metricRegistry.register("jvm.gc", new GarbageCollectorMetricSet());
    metricRegistry.register("jvm.memory", new MemoryUsageGaugeSet());
    metricRegistry.register("jvm.threads", new ThreadStatesGaugeSet());
  }


  @Override
  public int order() {
    return 0;
  }

  @Override
  public void start() throws Exception {
    jmxReporter = JmxReporter.forRegistry(metricRegistry).build();
    jmxReporter.start();
  }

  @Override
  public void stop() throws Exception {
    jmxReporter.stop();
  }
}
