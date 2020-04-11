package com.github.zhengframework.web;

import javax.inject.Inject;
import org.eclipse.jetty.server.Server;

public class JettyWebServer implements WebServer {

  private final Server server;

  private final JettyServerConfigurer jettyServerConfigurer;

  @Inject
  public JettyWebServer(Server server,
      JettyServerConfigurer jettyServerConfigurer) {
    this.server = server;
    this.jettyServerConfigurer = jettyServerConfigurer;
  }

  @Override
  public void start() throws Exception {
    jettyServerConfigurer.configure(server);
    server.start();
  }

  @Override
  public void stop() throws Exception {
    server.stop();
  }
}
