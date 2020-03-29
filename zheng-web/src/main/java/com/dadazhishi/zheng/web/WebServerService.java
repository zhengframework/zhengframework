package com.dadazhishi.zheng.web;

import com.dadazhishi.zheng.service.ServiceRegistry;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

/**
 * A Guava service to startup / shutdown the web server.
 */
@SuppressWarnings("UnstableApiUsage")
@Slf4j
public class WebServerService extends AbstractIdleService {

  private final WebServer server;

  @Inject
  public WebServerService(ServiceRegistry serviceRegistry, WebServer server) {
    this.server = server;
    serviceRegistry.add(this);
  }

  @Override
  protected void startUp() throws Exception {
    log.debug("Starting web server");
    server.start();
  }

  @Override
  protected void shutDown() throws Exception {
    log.debug("Stopping web server");
    server.stop();
  }
}
