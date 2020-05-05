package com.github.zhengframework.rest.metrics;

/*-
 * #%L
 * zheng-rest
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

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.ResponseMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Joiner;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

@Provider
@ConstrainedTo(RuntimeType.SERVER)
public class MetricsFeature implements DynamicFeature {

  private final MetricRegistry registry;
  private ConcurrentMap<Method, MeterInterceptor> meters = new ConcurrentHashMap<>();
  private ConcurrentMap<Method, TimedInterceptor> timers = new ConcurrentHashMap<>();
  private ConcurrentMap<Method, CountedInterceptor> counters = new ConcurrentHashMap<>();
  private ConcurrentMap<Method, ResponseMeteredInterceptor> responseMeters =
      new ConcurrentHashMap<>();

  public MetricsFeature(MetricRegistry registry) {
    this.registry = registry;
  }

  @Override
  public void configure(ResourceInfo resourceInfo, FeatureContext context) {
    Method resourceMethod = resourceInfo.getResourceMethod();
    if (timers.containsKey(resourceMethod)) {
      context.register(timers.get(resourceMethod));
    } else {
      if (resourceMethod.isAnnotationPresent(Timed.class)) {
        final Timed annotation = resourceMethod.getAnnotation(Timed.class);
        final String name = chooseName(annotation.name(), annotation.absolute(), resourceInfo);
        final Timer timer = registry.timer(name);
        timers.putIfAbsent(resourceMethod, new TimedInterceptor(timer));
        context.register(timers.get(resourceMethod));
      }
    }

    if (meters.containsKey(resourceMethod)) {
      context.register(meters.get(resourceMethod));
    } else {
      if (resourceMethod.isAnnotationPresent(Metered.class)) {
        final Metered annotation = resourceMethod.getAnnotation(Metered.class);
        final String name = chooseName(annotation.name(), annotation.absolute(), resourceInfo);
        final Meter meter = registry.meter(name);
        meters.putIfAbsent(resourceMethod, new MeterInterceptor(meter));
        context.register(meters.get(resourceMethod));
      }
    }
    if (counters.containsKey(resourceMethod)) {
      context.register(counters.get(resourceMethod));
    } else {
      if (resourceMethod.isAnnotationPresent(Counted.class)) {
        Counted annotation = resourceMethod.getAnnotation(Counted.class);
        final String name = chooseName(annotation.name(), annotation.absolute(), resourceInfo);
        Counter counter = registry.counter(name);
        counters.putIfAbsent(resourceMethod, new CountedInterceptor(counter));
        context.register(counters.get(resourceMethod));
      }
    }

    if (responseMeters.containsKey(resourceMethod)) {
      context.register(responseMeters.get(resourceMethod));
    } else {
      if (resourceMethod.isAnnotationPresent(ResponseMetered.class)) {
        ResponseMetered annotation = resourceMethod.getAnnotation(ResponseMetered.class);
        final String name = chooseName(annotation.name(), annotation.absolute(), resourceInfo);
        List<Meter> meters =
            Collections.unmodifiableList(
                Arrays.asList(
                    registry.meter(MetricRegistry.name(name, "1xx-responses")), // 1xx
                    registry.meter(MetricRegistry.name(name, "2xx-responses")), // 2xx
                    registry.meter(MetricRegistry.name(name, "3xx-responses")), // 3xx
                    registry.meter(MetricRegistry.name(name, "4xx-responses")), // 4xx
                    registry.meter(MetricRegistry.name(name, "5xx-responses")) // 5xx
                ));
        responseMeters.putIfAbsent(resourceMethod, new ResponseMeteredInterceptor(meters));
        context.register(responseMeters.get(resourceMethod));
      }
    }
  }

  private String chooseName(String explicitName, boolean absolute, ResourceInfo resourceInfo) {
    if (explicitName != null && !explicitName.isEmpty()) {
      if (absolute) {
        return explicitName;
      }
      return MetricRegistry.name(getName(resourceInfo), explicitName);
    }

    return getName(resourceInfo);
  }

  private String getName(ResourceInfo resourceInfo) {
    return getMethod(resourceInfo.getResourceMethod()) + " - " + getPath(resourceInfo);
  }

  private String getPath(ResourceInfo resourceInfo) {
    String rootPath = null;
    String methodPath = null;

    if (resourceInfo.getResourceClass().isAnnotationPresent(Path.class)) {
      rootPath = resourceInfo.getResourceClass().getAnnotation(Path.class).value();
    }

    if (resourceInfo.getResourceMethod().isAnnotationPresent(Path.class)) {
      methodPath = resourceInfo.getResourceMethod().getAnnotation(Path.class).value();
    }

    return Joiner.on("/").skipNulls().join(rootPath, methodPath);
  }

  private String getMethod(Method resourceMethod) {
    if (resourceMethod.isAnnotationPresent(GET.class)) {
      return HttpMethod.GET;
    }
    if (resourceMethod.isAnnotationPresent(POST.class)) {
      return HttpMethod.POST;
    }
    if (resourceMethod.isAnnotationPresent(PUT.class)) {
      return HttpMethod.PUT;
    }
    if (resourceMethod.isAnnotationPresent(DELETE.class)) {
      return HttpMethod.DELETE;
    }
    if (resourceMethod.isAnnotationPresent(HEAD.class)) {
      return HttpMethod.HEAD;
    }
    if (resourceMethod.isAnnotationPresent(OPTIONS.class)) {
      return HttpMethod.OPTIONS;
    }

    throw new IllegalStateException(
        "Resource method without GET, POST, PUT, DELETE, HEAD or OPTIONS annotation");
  }
}
