package com.dadazhishi.zheng.rest.metrics;

import com.codahale.metrics.Meter;
import java.io.IOException;
import java.util.List;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

public class ResponseMeteredInterceptor implements ContainerResponseFilter {

  private final List<Meter> meters;

  public ResponseMeteredInterceptor(List<Meter> meters) {
    this.meters = meters;
  }

  @Override
  public void filter(ContainerRequestContext requestContext,
      ContainerResponseContext responseContext) throws IOException {
    final int responseStatus = responseContext.getStatus() / 100;
    if (responseStatus >= 1 && responseStatus <= 5) {
      meters.get(responseStatus - 1).mark();
    }
  }
}
