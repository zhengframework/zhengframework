package com.github.zhengframework.web;

import com.google.common.base.Preconditions;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import java.util.EnumSet;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * Simple Jetty-based embedded web server which configures itself from a bound WebConfig and serves
 * the GuiceFilter so you can manage web content with Guice ServletModules. Also clever enough to
 * add any EventListener objects found in the injector bindings.
 */
@Slf4j
@Singleton
public class JettyWebServer implements WebServer {

  private WebConfig webConfig;
  private EventListenerClassScanner eventListenerScanner;
  private HandlerClassScanner handlerScanner;

  private Server server;

  @Override
  public void init(Provider<WebConfig> webConfigProvider, Provider<Injector> injectorProvider) {
    this.webConfig = webConfigProvider.get();
    Injector injector = injectorProvider.get();
    this.eventListenerScanner = new EventListenerClassScanner(injector);
    this.handlerScanner = new HandlerClassScanner(injector);
  }

  /**
   * Start the web server. Does not block.
   *
   * @see Server#start()
   */
  public void start() throws Exception {
    Preconditions.checkState(server == null, "Server already started");

    server = createServer(webConfig);

    final ServletContextHandler context = createRootServletContextHandler(webConfig);

    FilterHolder filterHolder = new FilterHolder();
    filterHolder.setDisplayName("GuiceFilter");
    filterHolder.setName("GuiceFilter");
    filterHolder.setAsyncSupported(true);
    filterHolder.setHeldClass(GuiceFilter.class);
    context.addFilter(filterHolder, "/*", EnumSet.allOf(DispatcherType.class));

    context.addServlet(DefaultServlet.class, "/");

    eventListenerScanner.accept(context::addEventListener);

    final HandlerCollection handlers = new HandlerCollection();

    handlers.addHandler(context);

    handlerScanner.accept(handlers::addHandler);

    server.setHandler(handlers);

    server.start();
  }

  /**
   * Overrideable method to create the root ServletContextHandler. This can be used so that the
   * Server can have a ServletContextHandler that will be able to handle Sessions for example. By
   * default we create a bare-bones ServletContextHandler that is not set up to handle Sessions.
   *
   * @param webConfig WebConfig
   * @return ServletContextHandler
   */
  protected ServletContextHandler createRootServletContextHandler(WebConfig webConfig) {
    return new ServletContextHandler(null, webConfig.getContextPath(),
        ServletContextHandler.SESSIONS);
  }

  /**
   * Overrideable method to create the initial jetty Server. We need to draw a lot more
   * configuration parameters into WebConfig, but for now this gives users a hook to satisfy their
   * needs. Bind a subclass to WebConfig, subclass this WebServer, and change behavior to whatever
   * you want.
   *
   * @param webConfig WebConfig
   * @return Server
   */
  protected Server createServer(WebConfig webConfig) {
    return new Server(webConfig.getPort());
  }

  /**
   * signal the web server to stop
   */
  public void stop() throws Exception {
    Preconditions.checkState(server != null, "Server not started");
    server.stop();
  }
}
