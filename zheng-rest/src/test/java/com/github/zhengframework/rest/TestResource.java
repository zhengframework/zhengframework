package com.github.zhengframework.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path(TestResource.PATH)
public class TestResource {

  public static final String PATH = "test";
  public static final String MESSAGE = "Hello World!";

  private final MySingleton service;
  private final PerRequestService requestService;

  @Inject
  TestResource(MySingleton service, PerRequestService perRequestService) {
    this.service = service;
    this.requestService = perRequestService;
  }

  @Path("inject")
  @GET
  public String get() {
    return "Success: " + service.call() + ", " + requestService.call();
  }

  @GET
  public String sayHello() {
    return MESSAGE;
  }

}
