package com.github.zhengframework.metrics;

/*-
 * #%L
 * zheng-metrics
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.codahale.metrics.MetricRegistry;
import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.palominolabs.metrics.guice.GaugeInstanceClassMetricNamer;
import com.palominolabs.metrics.guice.MetricNamer;
import com.palominolabs.metrics.guice.MetricsInstrumentationModule;
import com.palominolabs.metrics.guice.annotation.AnnotationResolver;
import com.palominolabs.metrics.guice.annotation.MethodAnnotationResolver;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = false)
public class MetricsModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    MetricsConfig metricsConfig =
        ConfigurationBeanMapper.resolve(getConfiguration(), "", MetricsConfig.class);
    bind(MetricsConfig.class).toInstance(metricsConfig);

    if (metricsConfig.isEnable()) {
      MetricRegistry metricRegistry = new MetricRegistry();
      MetricNamer metricNamer = new GaugeInstanceClassMetricNamer();
      AnnotationResolver annotationResolver = new MethodAnnotationResolver();
      bind(MetricRegistry.class).toInstance(metricRegistry);
      MetricsInstrumentationModule metricsInstrumentationModule =
          MetricsInstrumentationModule.builder()
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
