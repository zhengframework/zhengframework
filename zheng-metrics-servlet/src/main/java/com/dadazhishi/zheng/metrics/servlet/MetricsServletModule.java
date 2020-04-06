package com.dadazhishi.zheng.metrics.servlet;

import com.codahale.metrics.servlets.AdminServlet;
import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationAware;
import com.dadazhishi.zheng.configuration.ConfigurationBeanMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.servlet.ServletModule;
import javax.inject.Singleton;

public class MetricsServletModule extends ServletModule implements ConfigurationAware {

  private Configuration configuration;

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  protected void configureServlets() {
    Preconditions.checkArgument(configuration != null, "configuration is null");
    MetricsServletConfig metricsServletConfig = ConfigurationBeanMapper
        .resolve(configuration, MetricsServletConfig.PREFIX, MetricsServletConfig.class);
    if (metricsServletConfig.isEnable()) {
      String path = metricsServletConfig.getPath();
      if (Strings.isNullOrEmpty(path)) {
        serve("/metrics/*").with(new AdminServlet());
      } else {
        serve(path + "/*").with(new AdminServlet());
      }
      bind(HealthCheckScanner.class);
      bind(MetricsServletContextListener.class).in(Singleton.class);
      bind(HealthCheckServletContextListener.class).in(Singleton.class);

    }

  }
}
