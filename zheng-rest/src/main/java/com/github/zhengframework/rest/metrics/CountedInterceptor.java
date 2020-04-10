package com.github.zhengframework.rest.metrics;

import com.codahale.metrics.Counter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

public class CountedInterceptor implements ContainerRequestFilter {

  private final Counter counter;

  public CountedInterceptor(Counter counter) {
    this.counter = counter;
  }

  @Override
  public void filter(ContainerRequestContext requestContext) {
    counter.inc();
  }
}
