package com.dadazhishi.zheng.metrics.servlet;

import com.codahale.metrics.servlets.AdminServlet;
import com.google.inject.servlet.ServletModule;
import javax.inject.Singleton;

public class MetricsServletModule extends ServletModule {

  private final String path;

  public MetricsServletModule() {
    this(null);
  }

  public MetricsServletModule(String path) {
    this.path = path;
  }

  @Override
  protected void configureServlets() {
    if (path == null) {
      serve("/metrics/*").with(new AdminServlet());
    }

    bind(MetricsServletContextListener.class).in(Singleton.class);
    bind(HealthCheckServletContextListener.class).in(Singleton.class);

  }
}
