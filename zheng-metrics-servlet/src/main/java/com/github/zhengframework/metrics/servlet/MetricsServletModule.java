package com.github.zhengframework.metrics.servlet;

import com.codahale.metrics.servlets.AdminServlet;
import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationAware;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.metrics.MetricsConfig;
import com.github.zhengframework.web.PathUtils;
import com.github.zhengframework.web.WebConfig;
import com.google.common.base.Preconditions;
import com.google.inject.servlet.ServletModule;
import java.util.Map;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MetricsServletModule extends ServletModule implements ConfigurationAware {

  private Configuration configuration;

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  protected void configureServlets() {
    Preconditions.checkArgument(configuration != null, "configuration is null");

    Map<String, MetricsConfig> metricsConfigMap = ConfigurationBeanMapper
        .resolve(configuration, MetricsConfig.class);
    MetricsConfig metricsConfig = metricsConfigMap.getOrDefault("", new MetricsConfig());

    Map<String, MetricsServletConfig> metricsServletConfigMap = ConfigurationBeanMapper
        .resolve(configuration, MetricsServletConfig.class);
    MetricsServletConfig metricsServletConfig = metricsServletConfigMap
        .getOrDefault("", new MetricsServletConfig());
    if (metricsConfig.isEnable()) {
      if (metricsServletConfig.isEnable()) {
        Map<String, WebConfig> webConfigMap = ConfigurationBeanMapper
            .resolve(configuration, WebConfig.class);
        WebConfig webConfig = webConfigMap.getOrDefault("", new WebConfig());

        String path = PathUtils.fixPath(metricsServletConfig.getPath());
        serve(path + "/*").with(AdminServlet.class);
        bind(AdminServlet.class).in(Singleton.class);
        log.info("Metrics Admin Page: {}", "http://localhost:" + webConfig.getPort() + PathUtils
            .fixPath(webConfig.getContextPath(), path) + "/");
        bind(HealthCheckScanner.class);
        bind(MetricsServletContextListener.class).in(Singleton.class);
        bind(HealthCheckServletContextListener.class).in(Singleton.class);
      } else {
        log.warn("MetricsServletModule is disable");
      }
    } else {
      log.warn("MetricsModule is disable");
    }
  }
}
