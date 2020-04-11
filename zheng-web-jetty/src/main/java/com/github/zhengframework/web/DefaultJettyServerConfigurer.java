package com.github.zhengframework.web;

import com.google.common.collect.Lists;
import com.google.inject.servlet.GuiceFilter;
import java.util.EnumSet;
import java.util.Set;
import javax.inject.Inject;
import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.ServerContainer;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

@Slf4j
public class DefaultJettyServerConfigurer implements JettyServerConfigurer {

  private final WebConfig webConfig;
  private final Set<ServerEndpointConfig> serverEndpointConfigSet;
  private final Set<Class<? extends WebSocketEndpoint>> annotatedEndpoints;
  private final EventListenerClassScanner eventListenerScanner;
  private final HandlerClassScanner handlerScanner;
  private final GuiceServerEndpointConfigurator guiceServerEndpointConfigurator;

  @Inject
  public DefaultJettyServerConfigurer(WebConfig webConfig,
      Set<ServerEndpointConfig> serverEndpointConfigSet,
      Set<Class<? extends WebSocketEndpoint>> annotatedEndpoints,
      EventListenerClassScanner eventListenerScanner,
      HandlerClassScanner handlerScanner,
      GuiceServerEndpointConfigurator guiceServerEndpointConfigurator) {
    this.webConfig = webConfig;
    this.serverEndpointConfigSet = serverEndpointConfigSet;
    this.annotatedEndpoints = annotatedEndpoints;
    this.eventListenerScanner = eventListenerScanner;
    this.handlerScanner = handlerScanner;
    this.guiceServerEndpointConfigurator = guiceServerEndpointConfigurator;
  }

  @Override
  public void configure(Server server) {
    final ServerConnector connector = new ServerConnector(server);
    log.info("jetty web server port={} contextPath={}", webConfig.getPort(),
        webConfig.getContextPath());
    connector.setPort(webConfig.getPort());

    server.addConnector(connector);

    final ServletContextHandler context = new ServletContextHandler(null,
        webConfig.getContextPath(),
        ServletContextHandler.SESSIONS);
    initWebSocket(context);

    final FilterHolder filterHolder = new FilterHolder();
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

  }

  private void initWebSocket(ServletContextHandler context) {
    try {
      ServerContainer serverContainer = WebSocketServerContainerInitializer.initialize(context);
      for (ServerEndpointConfig serverEndpointConfig : serverEndpointConfigSet) {
        serverContainer.addEndpoint(serverEndpointConfig);
      }

      for (Class<? extends WebSocketEndpoint> clazz : annotatedEndpoints) {
        ServerEndpoint annotation = clazz.getAnnotation(ServerEndpoint.class);
        if (annotation != null) {
          serverContainer
              .addEndpoint(ServerEndpointConfig.Builder.create(clazz, annotation.value())
                  .configurator(guiceServerEndpointConfigurator)
                  .decoders(Lists.newArrayList(annotation.decoders()))
                  .encoders(Lists.newArrayList(annotation.encoders()))
                  .subprotocols(Lists.newArrayList(annotation.subprotocols()))
                  .build());
        }
      }
    } catch (DeploymentException | ServletException e) {
      throw new RuntimeException("add WebSocketServerContainer fail", e);
    }
  }

}
