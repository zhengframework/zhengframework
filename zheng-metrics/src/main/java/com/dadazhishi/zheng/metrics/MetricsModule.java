package com.dadazhishi.zheng.metrics;

import com.codahale.metrics.MetricRegistry;
import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationObjectMapper;
import com.dadazhishi.zheng.configuration.ConfigurationSupport;
import com.dadazhishi.zheng.service.ServicesModule;
import com.google.inject.AbstractModule;
import com.palominolabs.metrics.guice.GaugeInstanceClassMetricNamer;
import com.palominolabs.metrics.guice.MetricNamer;
import com.palominolabs.metrics.guice.MetricsInstrumentationModule;
import com.palominolabs.metrics.guice.annotation.AnnotationResolver;
import com.palominolabs.metrics.guice.annotation.MethodAnnotationResolver;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * Add the metrics-guice MetricsInstrumentationModule to scan for metrics annotations.
 */
@Slf4j
@EqualsAndHashCode(callSuper = false, of = {})
public class MetricsModule extends AbstractModule implements ConfigurationSupport {

  private Configuration configuration;

  @Override
  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }


  private final MetricRegistry metricRegistry = new MetricRegistry();
  private final MetricNamer metricNamer = new GaugeInstanceClassMetricNamer();
  private final AnnotationResolver annotationResolver = new MethodAnnotationResolver();


  @Override
  protected void configure() {
    MetricsConfig metricsServletConfig = ConfigurationObjectMapper
        .resolve(configuration, MetricsConfig.NAMESPACE, MetricsConfig.class);
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
