package com.github.zhengframework.rest.metrics;

import com.codahale.metrics.Meter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

public class MeterInterceptor implements ContainerRequestFilter {

  private final Meter meter;

  public MeterInterceptor(Meter meter) {
    this.meter = meter;
  }

  @Override
  public void filter(ContainerRequestContext requestContext) {
    meter.mark();
  }
}
