package com.github.zhengframework.metrics;

import com.codahale.metrics.MetricRegistry;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.core.Configuration;
import com.github.zhengframework.core.ConfigurationAware;
import com.github.zhengframework.service.ServicesModule;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.palominolabs.metrics.guice.GaugeInstanceClassMetricNamer;
import com.palominolabs.metrics.guice.MetricNamer;
import com.palominolabs.metrics.guice.MetricsInstrumentationModule;
import com.palominolabs.metrics.guice.annotation.AnnotationResolver;
import com.palominolabs.metrics.guice.annotation.MethodAnnotationResolver;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = false)
public class MetricsModule extends AbstractModule implements ConfigurationAware {

  private final MetricRegistry metricRegistry = new MetricRegistry();
  private final MetricNamer metricNamer = new GaugeInstanceClassMetricNamer();
  private final AnnotationResolver annotationResolver = new MethodAnnotationResolver();
  private Configuration configuration;

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  protected void configure() {
    Preconditions.checkArgument(configuration != null, "configuration is null");
    MetricsConfig metricsServletConfig = ConfigurationBeanMapper
        .resolve(configuration, MetricsConfig.PREFIX, MetricsConfig.class);
    if (metricsServletConfig.isEnable()) {
      install(new ServicesModule());

      bind(MetricRegistry.class).toInstance(metricRegistry);
      bind(MetricNamer.class).toInstance(metricNamer);
      bind(AnnotationResolver.class).toInstance(annotationResolver);
      MetricsInstrumentationModule metricsInstrumentationModule = MetricsInstrumentationModule
          .builder()
          .withMetricRegistry(metricRegistry)
          .withMetricNamer(metricNamer)
          .withAnnotationMatcher(annotationResolver)
          .build();
      install(metricsInstrumentationModule);
      bind(MetricsService.class).asEagerSingleton();
    } else {
      log.warn("MetricsModule is not bind, zheng.metrics.enable=false");
    }

  }
}
