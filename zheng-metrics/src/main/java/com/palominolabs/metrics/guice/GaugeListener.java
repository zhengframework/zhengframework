package com.palominolabs.metrics.guice;

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
import com.codahale.metrics.annotation.Gauge;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.palominolabs.metrics.guice.annotation.AnnotationResolver;
import java.lang.reflect.Method;

/** A listener which adds gauge injection listeners to classes with gauges. */
public class GaugeListener implements TypeListener {

  private final MetricRegistry metricRegistry;
  private final MetricNamer metricNamer;
  private final AnnotationResolver annotationResolver;

  public GaugeListener(
      MetricRegistry metricRegistry,
      MetricNamer metricNamer,
      final AnnotationResolver annotationResolver) {
    this.metricRegistry = metricRegistry;
    this.metricNamer = metricNamer;
    this.annotationResolver = annotationResolver;
  }

  @Override
  public <I> void hear(final TypeLiteral<I> literal, TypeEncounter<I> encounter) {
    Class<? super I> klass = literal.getRawType();
    final Class<?> instanceType = klass;

    do {
      for (Method method : klass.getDeclaredMethods()) {
        if (method.isSynthetic()) {
          continue;
        }

        final Gauge annotation = annotationResolver.findAnnotation(Gauge.class, method);
        if (annotation != null) {
          if (method.getParameterTypes().length == 0) {
            final String metricName = metricNamer.getNameForGauge(instanceType, method, annotation);

            // deprecated method in java 9, but replacement is not available in java 8
            if (!method.isAccessible()) {
              method.setAccessible(true);
            }

            encounter.register(new GaugeInjectionListener<>(metricRegistry, metricName, method));
          } else {
            encounter.addError(
                "Method %s is annotated with @Gauge but requires parameters.", method);
          }
        }
      }
    } while ((klass = klass.getSuperclass()) != null);
  }
}
