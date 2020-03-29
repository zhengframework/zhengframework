package com.dadazhishi.zheng.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/hello/rest")
public class HelloResource {

  @GET
  public String hello() {
    return "hello, world";
  }
}
