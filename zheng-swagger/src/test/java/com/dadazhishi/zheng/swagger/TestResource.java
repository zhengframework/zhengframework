package com.github.zhengframework.swagger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/test")
public class TestResource {

  @GET
  @Path("/{a}/{b}")
  public String test(@PathParam("a") String a, @PathParam("b") Integer b) {
    return a + "/" + b;
  }
}
