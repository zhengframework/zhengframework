package com.github.zhengframework.web;

import com.google.inject.Injector;
import javax.inject.Inject;
import javax.inject.Provider;
import org.eclipse.jetty.server.Server;

public class JettyServer implements WebServer {

  private final Server server;

  private final JettyServerConfigurer jettyServerConfigurer;

  @Inject
  public JettyServer(Server server,
      JettyServerConfigurer jettyServerConfigurer) {
    this.server = server;
    this.jettyServerConfigurer = jettyServerConfigurer;
  }

  @Override
  public void init(Provider<WebConfig> webConfigProvider, Provider<Injector> injectorProvider) {

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
