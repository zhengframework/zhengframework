package com.github.zhengframework.web;

import com.github.zhengframework.core.Service;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WebServerService implements Service {

  private final WebServer server;

  @Inject
  public WebServerService(WebServer server) {
    this.server = server;
  }

  @Override
  public int priority() {
    return Integer.MIN_VALUE;
  }

  @Override
  public void start() throws Exception {
    server.start();
  }

  @Override
  public void stop() throws Exception {
    server.stop();
  }
}
