package com.github.zhengframework.web;

import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationAware;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.service.ServicesModule;
import com.google.common.base.Preconditions;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.OptionalBinder;
import com.google.inject.servlet.ServletModule;
import java.util.Iterator;
import java.util.ServiceLoader;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.websocket.server.ServerEndpointConfig;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@EqualsAndHashCode(callSuper = false, of = {})
public class WebModule extends ServletModule implements ConfigurationAware {

  private Configuration configuration;

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  protected void configureServlets() {
    Preconditions.checkArgument(configuration != null, "configuration is null");
    install(new ServicesModule());
    bind(WebServerService.class).asEagerSingleton();
    WebConfig webConfig = ConfigurationBeanMapper
        .resolve(configuration, WebConfig.PREFIX, WebConfig.class);
    bind(WebConfig.class).toInstance(webConfig);

    // websocket support
    OptionalBinder
        .newOptionalBinder(binder(), new TypeLiteral<WebSocketEndpoint>() {
        });
    OptionalBinder.newOptionalBinder(binder(), new TypeLiteral<ServerEndpointConfig>() {
    });

  }

  @Singleton
  @Provides
  public WebServer webServer(Provider<WebConfig> webConfigProvider,
      Provider<Injector> injectorProvider) {
    ServiceLoader<WebServer> webServers = ServiceLoader.load(WebServer.class);
    Iterator<WebServer> iterator = webServers.iterator();
    if (iterator.hasNext()) {
      WebServer webServer = iterator.next();
      log.info("find WebServer class [{}]", webServer.getClass());
      webServer.init(webConfigProvider, injectorProvider);
      return webServer;
    } else {
      throw new IllegalStateException("need WebServer");
    }
  }

}
