package com.dadazhishi.zheng.metrics.servlet;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.MetricsServlet;
import javax.inject.Inject;

public class MetricsServletContextListener extends MetricsServlet.ContextListener {

  private final MetricRegistry metricRegistry;

  @Inject
  public MetricsServletContextListener(MetricRegistry metricRegistry) {
    this.metricRegistry = metricRegistry;
  }

  @Override
  protected MetricRegistry getMetricRegistry() {
    return metricRegistry;
  }
}
