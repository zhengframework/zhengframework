package com.dadazhishi.zheng.metrics.jersey;

import com.codahale.metrics.MetricRegistry;
import com.dadazhishi.zheng.metrics.SettableGauge;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class GaugeMethodInterceptor implements MethodInterceptor {

  private final SettableGauge<Object> gauge;

  public GaugeMethodInterceptor(MetricRegistry metricRegistry, String metricName) {
    gauge = metricRegistry.register(metricName, new SettableGauge<>());
  }

  @Override
  public Object invoke(MethodInvocation methodInvocation) throws Throwable {
    try {
      Object proceed = methodInvocation.proceed();
      gauge.setValue(proceed);
      return proceed;
    } catch (Throwable throwable) {
      return new RuntimeException(throwable);
    }
  }
}
