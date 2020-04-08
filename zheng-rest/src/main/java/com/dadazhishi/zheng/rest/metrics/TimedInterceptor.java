package com.dadazhishi.zheng.rest.metrics;

import com.codahale.metrics.Timer;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

public class TimedInterceptor implements ContainerRequestFilter, ContainerResponseFilter {

  private final Timer timer;
  private Timer.Context context;

  public TimedInterceptor(Timer timer) {
    this.timer = timer;
  }

  @Override
  public void filter(ContainerRequestContext requestContext) {
    context = timer.time();
  }

  @Override
  public void filter(ContainerRequestContext requestContext,
      ContainerResponseContext responseContext) {

    context.stop();
  }
}
