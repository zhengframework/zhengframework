package com.github.zhengframework.web;


import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UndertowWebServer implements WebServer {

  private final Undertow.Builder server;
  private final UndertowServerConfigurer undertowServerConfigurer;
  private Undertow undertow;

  @Inject
  public UndertowWebServer(Builder server,
      UndertowServerConfigurer undertowServerConfigurer) {
    this.server = server;
    this.undertowServerConfigurer = undertowServerConfigurer;
  }

  @Override
  public void start() throws Exception {
    undertowServerConfigurer.configure(server);
    undertow = server.build();
    undertow.start();
  }

  @Override
  public void stop() throws Exception {
    undertow.stop();
  }

}
