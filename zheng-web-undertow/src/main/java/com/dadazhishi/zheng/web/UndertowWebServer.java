package com.dadazhishi.zheng.web;

import com.google.common.base.Preconditions;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.FilterInfo;
import io.undertow.servlet.api.ListenerInfo;
import io.undertow.servlet.util.ImmediateInstanceFactory;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.servlet.DispatcherType;

@Singleton
public class UndertowWebServer implements WebServer {

  private WebConfig webConfig;
  private EventListenerClassScanner eventListenerScanner;

  private Undertow server;

  @Override
  public void init(Provider<WebConfig> webConfigProvider, Provider<Injector> injectorProvider) {
    this.webConfig = webConfigProvider.get();
    Injector injector = injectorProvider.get();
    this.eventListenerScanner = new EventListenerClassScanner(injector);
  }

  /**
   * Start the web server. Does not block.
   */
  public void start() throws Exception {
    Preconditions.checkState(server == null, "Server already started");

    DeploymentInfo deploymentInfo = Servlets.deployment()
        .setClassLoader(UndertowWebServer.class.getClassLoader())
        .setContextPath(webConfig.getContextPath())
        .setDeploymentName("zheng-web-undertow-" + webConfig.getPort() + ".war");

    ;
    deploymentInfo.addFilter(new FilterInfo("GuiceFilter", GuiceFilter.class)
        .setAsyncSupported(true));
    deploymentInfo.addFilterUrlMapping("GuiceFilter", "/*", DispatcherType.REQUEST);

    eventListenerScanner
        .accept(thing -> deploymentInfo.addListener(new ListenerInfo(thing.getClass(),
            new ImmediateInstanceFactory<>(thing))));

    DeploymentManager manager = Servlets.defaultContainer().addDeployment(deploymentInfo);
    manager.deploy();
    PathHandler path = Handlers.path(Handlers.redirect(webConfig.getContextPath()))
        .addPrefixPath(webConfig.getContextPath(), manager.start());

    server = Undertow.builder()
        .addHttpListener(webConfig.getPort(), "localhost")
        .setHandler(path)
        .build();

    // Start the server
    server.start();
  }

  /**
   * signal the web server to stop
   */
  public void stop() throws Exception {
    Preconditions.checkState(server != null, "Server not started");
    server.stop();
  }
}
