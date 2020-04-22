package com.github.zhengframework.web;

import com.google.common.collect.Lists;
import com.google.inject.servlet.GuiceFilter;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.attribute.ExchangeAttributes;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.LearningPushHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.encoding.EncodingHandler;
import io.undertow.server.handlers.resource.ResourceManager;
import io.undertow.server.session.InMemorySessionManager;
import io.undertow.server.session.SessionAttachmentHandler;
import io.undertow.server.session.SessionCookieConfig;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultUndertowServerConfigurer implements UndertowServerConfigurer {

  private final WebConfig webConfig;
  private final EventListenerClassScanner eventListenerScanner;
  private final ClassIntrospecter classIntrospecter;
  private final Set<ServerEndpointConfig> serverEndpointConfigSet;
  private final Set<Class<? extends WebSocketEndpoint>> annotatedEndpoints;
  private final GuiceServerEndpointConfigurator guiceServerEndpointConfigurator;
  private final ResourceManager resourceManager;

  @Inject
  public DefaultUndertowServerConfigurer(WebConfig webConfig,
      EventListenerClassScanner eventListenerScanner,
      ClassIntrospecter classIntrospecter,
      Set<ServerEndpointConfig> serverEndpointConfigSet,
      Set<Class<? extends WebSocketEndpoint>> annotatedEndpoints,
      GuiceServerEndpointConfigurator guiceServerEndpointConfigurator,
      ResourceManager resourceManager) {
    this.webConfig = webConfig;
    this.eventListenerScanner = eventListenerScanner;
    this.classIntrospecter = classIntrospecter;
    this.serverEndpointConfigSet = serverEndpointConfigSet;
    this.annotatedEndpoints = annotatedEndpoints;
    this.guiceServerEndpointConfigurator = guiceServerEndpointConfigurator;
    this.resourceManager = resourceManager;
  }

  @Override
  public void configure(Undertow.Builder server) {
    try {
      DeploymentInfo deploymentInfo = Servlets.deployment()
          .setClassIntrospecter(classIntrospecter)
          .setClassLoader(Thread.currentThread().getContextClassLoader())
          .setContextPath("/")
          .setDeploymentName("zheng-web-undertow-" + webConfig.getPort() + ".war");

      eventListenerScanner
          .accept(thing -> deploymentInfo.addListener(new ListenerInfo(thing.getClass(),
              new ImmediateInstanceFactory<>(thing))));

      deploymentInfo.addFilter(new FilterInfo("GuiceFilter", GuiceFilter.class)
          .setAsyncSupported(true));
      deploymentInfo.addFilterUrlMapping("GuiceFilter", "/*", DispatcherType.REQUEST);

      deploymentInfo.setResourceManager(resourceManager);

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

      HttpHandler encodingHandler = new EncodingHandler.Builder().build(null)
          .wrap(manager.start());
      rootHandler.addPrefixPath(webConfig.getContextPath(), encodingHandler);
      rootHandler.addPrefixPath(webConfig.getWebSocketPath(), websocketManager.start());

      server.setHandler(new SessionAttachmentHandler(
          new LearningPushHandler(100, -1,
              Handlers.header(rootHandler,
                  "x-undertow-transport", ExchangeAttributes.transportProtocol())),
          new InMemorySessionManager("sessionManager"), new SessionCookieConfig().setSecure(true)
          .setHttpOnly(true)
      ));

//      server.setHandler(rootHandler);
    } catch (ServletException e) {
      throw new RuntimeException(e);
    }
  }
}
