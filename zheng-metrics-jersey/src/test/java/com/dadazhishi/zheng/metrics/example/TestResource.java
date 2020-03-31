package com.dadazhishi.zheng.metrics.example;

import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Gauge;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
public class TestResource {

  private final TestService testService;

  @Inject
  public TestResource(TestService testService) {
    this.testService = testService;
  }

  @Gauge
  @Metered
  @Timed
  @Counted(monotonic = true)
  @GET
  public String hello() {
    return "hello " + testService.count();
  }

  @ExceptionMetered(cause = RuntimeException.class)
  @GET
  @Path("/ex")
  public String ex() {
    throw new RuntimeException("111");
  }

}
