package com.dadazhishi.zheng.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Path(TestResource.PATH)
public class TestResource {

  public static final String PATH = "test";
  public static final String MESSAGE = "Hello World!";

  @Path("inject")
  @GET
  public String getit(@Context HttpServletRequest request) {
    return "Success: " + request.getSession().getId() + ", " + request.getRequestURI();
  }

  @GET
  public String sayHello() {
    return MESSAGE;
  }

}
