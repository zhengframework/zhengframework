package com.github.zhengframework.web;

import com.google.common.collect.Lists;
import com.google.inject.servlet.GuiceFilter;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.ClassIntrospecter;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.FilterInfo;
import io.undertow.servlet.api.ListenerInfo;
import io.undertow.servlet.util.ImmediateInstanceFactory;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import java.util.Set;
import javax.inject.Inject;
import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Builder;

public class DefaultUndertowServerConfigurer implements UndertowServerConfigurer {

  private final WebConfig webConfig;
  private final EventListenerClassScanner eventListenerScanner;
  private final ClassIntrospecter classIntrospecter;
  private final Set<ServerEndpointConfig> serverEndpointConfigSet;
  private final Set<Class<? extends WebSocketEndpoint>> annotatedEndpoints;
  private final GuiceServerEndpointConfigurator guiceServerEndpointConfigurator;

  @Inject
  public DefaultUndertowServerConfigurer(WebConfig webConfig,
      EventListenerClassScanner eventListenerScanner,
      ClassIntrospecter classIntrospecter,
      Set<ServerEndpointConfig> serverEndpointConfigSet,
      Set<Class<? extends WebSocketEndpoint>> annotatedEndpoints,
      GuiceServerEndpointConfigurator guiceServerEndpointConfigurator) {
    this.webConfig = webConfig;
    this.eventListenerScanner = eventListenerScanner;
    this.classIntrospecter = classIntrospecter;
    this.serverEndpointConfigSet = serverEndpointConfigSet;
    this.annotatedEndpoints = annotatedEndpoints;
    this.guiceServerEndpointConfigurator = guiceServerEndpointConfigurator;
  }

  @Override
  public void configure(Undertow.Builder server) {
    try {
      DeploymentInfo deploymentInfo = Servlets.deployment()
          .setClassIntrospecter(classIntrospecter)
          .setClassLoader(UndertowWebServer.class.getClassLoader())
          .setContextPath("/")
          .setDeploymentName("zheng-web-undertow-" + webConfig.getPort() + ".war");

      eventListenerScanner
          .accept(thing -> deploymentInfo.addListener(new ListenerInfo(thing.getClass(),
              new ImmediateInstanceFactory<>(thing))));

      deploymentInfo.addFilter(new FilterInfo("GuiceFilter", GuiceFilter.class)
          .setAsyncSupported(true));
      deploymentInfo.addFilterUrlMapping("GuiceFilter", "/*", DispatcherType.REQUEST);

      DeploymentManager manager = Servlets.defaultContainer().addDeployment(deploymentInfo);
      manager.deploy();

      WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();

      webSocketDeploymentInfo.addProgramaticEndpoints(serverEndpointConfigSet);
      for (Class<? extends WebSocketEndpoint> clazz : annotatedEndpoints) {
        ServerEndpoint annotation = clazz.getAnnotation(ServerEndpoint.class);
        if (annotation != null) {
          ServerEndpointConfig serverEndpointConfig = Builder.create(clazz, annotation.value())
              .configurator(guiceServerEndpointConfigurator)
              .decoders(Lists.newArrayList(annotation.decoders()))
              .encoders(Lists.newArrayList(annotation.encoders()))
              .subprotocols(Lists.newArrayList(annotation.subprotocols()))
              .build();
          webSocketDeploymentInfo.addEndpoint(serverEndpointConfig);
        }
      }
      DeploymentInfo websocketDeployment = Servlets.deployment()
          .setClassIntrospecter(classIntrospecter)
          .setContextPath(webConfig.getContextPath())
          .setSecurityDisabled(true)
          .setAuthorizationManager(null)
          .addServletContextAttribute(WebSocketDeploymentInfo.ATTRIBUTE_NAME,
              webSocketDeploymentInfo)
          .setDeploymentName("zheng-web-undertow-websocket")
          .setClassLoader(Thread.currentThread().getContextClassLoader());
      DeploymentManager websocketManager = Servlets.defaultContainer()
          .addDeployment(websocketDeployment);
      websocketManager.deploy();

      PathHandler rootHandler = Handlers.path();

      rootHandler.addPrefixPath(webConfig.getContextPath(), manager.start());
      rootHandler.addExactPath(webConfig.getWebSocketPath(), websocketManager.start());
      server.setHandler(rootHandler);
      server.addHttpListener(webConfig.getPort(), "localhost");
    } catch (ServletException e) {
      throw new RuntimeException(e);
    }
  }
}
