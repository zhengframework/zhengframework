package com.github.zhengframework.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("first")
public class FirstResource {
  private final Man man;

  @Inject
  public FirstResource(Man man) {
    this.man = man;
  }

  @GET
  @Path("hello")
  public String hello() {
    return man.say();
  }
}
