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

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.palominolabs.metrics.guice.annotation.AnnotationResolver;
import java.lang.reflect.Method;
import javax.annotation.Nullable;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * A listener which adds method interceptors to methods that should be instrumented for exceptions
 */
public class ExceptionMeteredListener extends DeclaredMethodsTypeListener {

  private final MetricRegistry metricRegistry;
  private final MetricNamer metricNamer;
  private final AnnotationResolver annotationResolver;

  public ExceptionMeteredListener(MetricRegistry metricRegistry, MetricNamer metricNamer,
      final AnnotationResolver annotationResolver) {
    this.metricRegistry = metricRegistry;
    this.metricNamer = metricNamer;
    this.annotationResolver = annotationResolver;
  }

  @Nullable
  @Override
  protected MethodInterceptor getInterceptor(Method method) {
    final ExceptionMetered annotation = annotationResolver
        .findAnnotation(ExceptionMetered.class, method);
    if (annotation != null) {
      final Meter meter = metricRegistry
          .meter(metricNamer.getNameForExceptionMetered(method, annotation));
      return new ExceptionMeteredInterceptor(meter, annotation.cause());
    }
    return null;
  }
}
