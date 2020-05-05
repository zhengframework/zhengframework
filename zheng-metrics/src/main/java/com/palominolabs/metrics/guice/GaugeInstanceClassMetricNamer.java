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

import static com.codahale.metrics.MetricRegistry.name;

import com.codahale.metrics.annotation.Gauge;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;

/**
 * For gauges (which can reside in superclasses of the type being instantiated), this will use the
 * instantiated type for the resulting metric name no matter what superclass declares the gauge
 * method. This allows for a gauge located in a superclass to be used by multiple inheritors without
 * causing a duplicate metric name clash.
 *
 * For other metric types, which are not available on superclass methods, the declaring class (which
 * would be the same as the instantiated class) is used, as in {@link DeclaringClassMetricNamer}.
 */
public class GaugeInstanceClassMetricNamer extends DeclaringClassMetricNamer {

  @Nonnull
  @Override
  public String getNameForGauge(@Nonnull Class<?> instanceClass, @Nonnull Method method,
      @Nonnull Gauge gauge) {
    if (gauge.absolute()) {
      return gauge.name();
    }

    if (gauge.name().isEmpty()) {
      return name(instanceClass, method.getName(), GAUGE_SUFFIX);
    }

    return name(instanceClass, gauge.name());
  }
}
