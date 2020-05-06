package com.github.zhengframework.web.jetty;

/*-
 * #%L
 * zheng-web-jetty
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.github.zhengframework.web.EventListenerClassScanner;
import com.github.zhengframework.web.GuiceServerEndpointConfigurator;
import com.github.zhengframework.web.WebConfig;
import com.github.zhengframework.web.WebSocketEndpoint;
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
import javax.websocket.server.ServerEndpointConfig.Builder;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.http.HttpScheme;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.ResourceService;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;
import org.eclipse.jetty.websocket.api.InvalidWebSocketException;
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
  public DefaultJettyServerConfigurer(
      WebConfig webConfig,
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
    server.addBean(new ScheduledExecutorScheduler(null, false));

    server.addConnector(createHttpConnector(server));

    if (webConfig.isSsl()) {
      try {
        final ServerConnector httpsConnector = createHttpsConnector(server, webConfig.isHttp2());
        server.addConnector(httpsConnector);
      } catch (Exception e) {
        log.error("init sslContext fail", e);
      }
    }
    final ServletContextHandler context =
        new ServletContextHandler(null, webConfig.getContextPath(), ServletContextHandler.SESSIONS);

    final FilterHolder filterHolder = new FilterHolder();
    filterHolder.setDisplayName("GuiceFilter");
    filterHolder.setName("GuiceFilter");
    filterHolder.setAsyncSupported(true);
    filterHolder.setHeldClass(GuiceFilter.class);
    context.addFilter(filterHolder, "/*", EnumSet.allOf(DispatcherType.class));

    context.addServlet(DefaultServlet.class, "/");
    context.setWelcomeFiles(new String[] {"index.html"});

    eventListenerScanner.accept(context::addEventListener);

    initWebSocket(context);

    final HandlerCollection handlers = new HandlerCollection();

    handlers.addHandler(context);

    handlerScanner.accept(handlers::addHandler);

    Resource resource = Resource.newClassPathResource("META-INF/resources/");
    ResourceCollection resourceCollection = new ResourceCollection(resource);

    ResourceService resourceService = new ResourceService();
    resourceService.setEtags(true);
    resourceService.setAcceptRanges(true);
    resourceService.setDirAllowed(false);

    ResourceHandler resourceHandler = new ResourceHandler(resourceService);
    resourceHandler.setBaseResource(resourceCollection);
    handlers.addHandler(resourceHandler);

    handlers.addHandler(new DefaultHandler());
    server.setHandler(handlers);
  }

  private ServerConnector createHttpsConnector(Server server, boolean http2) throws Exception {
    SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
    sslContextFactory.setKeyStoreType(webConfig.getKeyStoreType());
    sslContextFactory.setKeyStorePath(webConfig.getKeyStorePath());
    sslContextFactory.setKeyStorePassword(webConfig.getKeyStorePassword());
    sslContextFactory.setKeyManagerPassword(webConfig.getKeyStorePassword());

    sslContextFactory.setTrustStorePath(webConfig.getTrustStorePath());
    sslContextFactory.setTrustStoreType(webConfig.getTrustStoreType());
    sslContextFactory.setTrustStorePassword(webConfig.getTrustStorePassword());
    HttpConfiguration httpConfig = createHttpConfiguration();

    // SSL HTTP Configuration
    HttpConfiguration httpsConfig = new HttpConfiguration(httpConfig);
    httpsConfig.addCustomizer(new SecureRequestCustomizer());

    HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(httpConfig);

    if (http2) {
      HTTP2ServerConnectionFactory http2ConnectionFactory =
          new HTTP2ServerConnectionFactory(httpConfig);
      ALPNServerConnectionFactory alpnServerConnectionFactory = new ALPNServerConnectionFactory();
      alpnServerConnectionFactory.setDefaultProtocol(httpConnectionFactory.getProtocol());
      sslContextFactory.setCipherComparator(HTTP2Cipher.COMPARATOR);
      sslContextFactory.setUseCipherSuitesOrder(true);
      SslConnectionFactory sslConnectionFactory =
          new SslConnectionFactory(sslContextFactory, alpnServerConnectionFactory.getProtocol());
      ServerConnector connector =
          new ServerConnector(
              server,
              sslConnectionFactory,
              alpnServerConnectionFactory,
              http2ConnectionFactory,
              httpConnectionFactory);
      connector.setPort(webConfig.getSslPort());
      return connector;
    } else {
      final ServerConnector httpsConnector =
          new ServerConnector(
              server,
              new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
              httpConnectionFactory);
      httpsConnector.setPort(webConfig.getSslPort());
      return httpsConnector;
    }
  }

  private HttpConfiguration createHttpConfiguration() {
    HttpConfiguration httpConfig = new HttpConfiguration();
    httpConfig.setSecureScheme(HttpScheme.HTTPS.asString());
    httpConfig.setSecurePort(webConfig.getSslPort());
    httpConfig.setOutputBufferSize(32768);
    httpConfig.setRequestHeaderSize(8192);
    httpConfig.setResponseHeaderSize(8192);
    httpConfig.setSendServerVersion(false);
    httpConfig.setSendDateHeader(false);
    httpConfig.setSendXPoweredBy(false);
    return httpConfig;
  }

  private ServerConnector createHttpConnector(Server server) {
    HttpConfiguration httpConfig = createHttpConfiguration();
    final ServerConnector connector =
        new ServerConnector(server, new HttpConnectionFactory(httpConfig));
    log.info("http port={}", webConfig.getPort());
    connector.setPort(webConfig.getPort());
    return connector;
  }

  private void initWebSocket(ServletContextHandler context) {
    try {
      ServerContainer serverContainer = WebSocketServerContainerInitializer.initialize(context);
      for (ServerEndpointConfig serverEndpointConfig : serverEndpointConfigSet) {
        BasePathServerEndpointConfig basePathServerEndpointConfig =
            new BasePathServerEndpointConfig(webConfig.getWebSocketPath(), serverEndpointConfig);
        log.info(
            "ServerEndpointConfig={} path={}",
            basePathServerEndpointConfig.getEndpointClass().getName(),
            basePathServerEndpointConfig.getPath());
        serverContainer.addEndpoint(basePathServerEndpointConfig);
      }

      for (Class<? extends WebSocketEndpoint> clazz : annotatedEndpoints) {
        ServerEndpoint annotation = clazz.getAnnotation(ServerEndpoint.class);
        if (annotation != null) {
          serverContainer.addEndpoint(createServerEndpointConfig(clazz, annotation));
        } else {
          throw new InvalidWebSocketException(
              "Unsupported WebSocket object, missing @" + ServerEndpoint.class + " annotation");
        }
      }
    } catch (DeploymentException | ServletException e) {
      throw new RuntimeException("add WebSocketServerContainer fail", e);
    }
  }

  private ServerEndpointConfig createServerEndpointConfig(
      Class<? extends WebSocketEndpoint> clazz, ServerEndpoint annotation) {

    ServerEndpointConfig serverEndpointConfig =
        Builder.create(clazz, annotation.value())
            .configurator(guiceServerEndpointConfigurator)
            .decoders(Lists.newArrayList(annotation.decoders()))
            .encoders(Lists.newArrayList(annotation.encoders()))
            .subprotocols(Lists.newArrayList(annotation.subprotocols()))
            .build();
    BasePathServerEndpointConfig basePathServerEndpointConfig =
        new BasePathServerEndpointConfig(webConfig.getWebSocketPath(), serverEndpointConfig);
    log.info(
        "WebSocketEndpoint={} path={}",
        basePathServerEndpointConfig.getEndpointClass().getName(),
        basePathServerEndpointConfig.getPath());
    return basePathServerEndpointConfig;
  }
}
