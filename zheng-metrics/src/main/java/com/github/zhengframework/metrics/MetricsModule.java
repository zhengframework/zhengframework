package com.github.zhengframework.metrics;

import com.codahale.metrics.MetricRegistry;
import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.palominolabs.metrics.guice.GaugeInstanceClassMetricNamer;
import com.palominolabs.metrics.guice.MetricNamer;
import com.palominolabs.metrics.guice.MetricsInstrumentationModule;
import com.palominolabs.metrics.guice.annotation.AnnotationResolver;
import com.palominolabs.metrics.guice.annotation.MethodAnnotationResolver;
import java.util.Map;
import java.util.Map.Entry;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = false)
public class MetricsModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    Map<String, MetricsConfig> metricsConfigMap = ConfigurationBeanMapper
        .resolve(getConfiguration(), MetricsConfig.class);
    for (Entry<String, MetricsConfig> entry : metricsConfigMap.entrySet()) {
      MetricsConfig metricsConfig = entry.getValue();
      if (metricsConfig.isEnable()) {
        MetricRegistry metricRegistry = new MetricRegistry();
        MetricNamer metricNamer = new GaugeInstanceClassMetricNamer();
        AnnotationResolver annotationResolver = new MethodAnnotationResolver();
//        OptionalBinder.newOptionalBinder(binder(),MetricRegistry.class)
//            .setDefault().to(MetricRegistry.class);
        bind(MetricRegistry.class).toInstance(metricRegistry);
        MetricsInstrumentationModule metricsInstrumentationModule = MetricsInstrumentationModule
            .builder()
            .withMetricRegistry(metricRegistry)
            .withMetricNamer(metricNamer)
            .withAnnotationMatcher(annotationResolver)
            .build();
        install(metricsInstrumentationModule);

        bind(MetricsService.class).asEagerSingleton();
      } else {
        log.warn("MetricsModule is disable");
      }
    }
  }
}
