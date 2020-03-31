package com.dadazhishi.zheng.metrics.example;

import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.Timed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
public class TestResource {

  private final TestService testService;

  @Inject
  public TestResource(
      TestService testService) {
    this.testService = testService;
  }

  @Timed
  @Counted
  @GET
  public String hello() {
    return "hello " + testService.count();
  }
}
