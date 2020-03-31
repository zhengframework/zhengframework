package com.dadazhishi.zheng.metrics.jersey;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Gauge;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.palominolabs.metrics.guice.CountedInterceptor;
import com.palominolabs.metrics.guice.ExceptionMeteredInterceptor;
import com.palominolabs.metrics.guice.MeteredInterceptor;
import com.palominolabs.metrics.guice.MetricNamer;
import com.palominolabs.metrics.guice.TimedInterceptor;
import com.palominolabs.metrics.guice.annotation.AnnotationResolver;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;
import org.aopalliance.intercept.ConstructorInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.InterceptionService;
import org.glassfish.hk2.utilities.BuilderHelper;
import org.jvnet.hk2.annotations.Service;

@Service
public class HK2InterceptionService implements InterceptionService {

  private final MetricRegistry metricRegistry;
  private final MetricNamer metricNamer;
  private final AnnotationResolver annotationResolver;


  private Map<String, List<MethodInterceptor>> methodInterceptorMap = new ConcurrentHashMap<>();

  @Inject
  public HK2InterceptionService(MetricRegistry metricRegistry,
      MetricNamer metricNamer,
      AnnotationResolver annotationResolver) {
    this.metricRegistry = metricRegistry;
    this.metricNamer = metricNamer;
    this.annotationResolver = annotationResolver;
  }

  @Override
  public Filter getDescriptorFilter() {
    return BuilderHelper.allFilter();
  }

  @Override
  public List<MethodInterceptor> getMethodInterceptors(Method method) {
    if (methodInterceptorMap.containsKey(method.getName())) {
      return methodInterceptorMap.get(method.getName());
    }
    List<MethodInterceptor> list = new ArrayList<>();
    final Counted annotationCounted = annotationResolver.findAnnotation(Counted.class, method);
    if (annotationCounted != null) {
      final Counter counter = metricRegistry
          .counter(metricNamer.getNameForCounted(method, annotationCounted));

      list.add(new CountedInterceptor(counter, annotationCounted));
    }
    final Timed annotationTimed = annotationResolver.findAnnotation(Timed.class, method);
    if (annotationTimed != null) {
      final Timer timer = metricRegistry
          .timer(metricNamer.getNameForTimed(method, annotationTimed));
      list.add(new TimedInterceptor(timer));
    }
    final Metered annotationMetered = annotationResolver.findAnnotation(Metered.class, method);
    if (annotationMetered != null) {
      final Meter meter = metricRegistry
          .meter(metricNamer.getNameForMetered(method, annotationMetered));
      list.add(new MeteredInterceptor(meter));
    }
    final ExceptionMetered annotationExceptionMetered = annotationResolver
        .findAnnotation(ExceptionMetered.class, method);
    if (annotationExceptionMetered != null) {
      final Meter meter = metricRegistry
          .meter(metricNamer.getNameForExceptionMetered(method, annotationExceptionMetered));
      list.add(
          new ExceptionMeteredInterceptor(meter, annotationExceptionMetered.cause()));
    }
    final Gauge annotationGauge = annotationResolver.findAnnotation(Gauge.class, method);
    if (annotationGauge != null) {
      Class<?> instanceType = method.getDeclaringClass();
      String metricName = metricNamer.getNameForGauge(instanceType, method, annotationGauge);
      list.add(new GaugeMethodInterceptor(metricRegistry, metricName));
    }
    if (!list.isEmpty()) {
      methodInterceptorMap.put(method.getName(), list);
      return list;
    }
    return null;
  }

  @Override
  public List<ConstructorInterceptor> getConstructorInterceptors(Constructor<?> constructor) {
    return null;
  }
}
