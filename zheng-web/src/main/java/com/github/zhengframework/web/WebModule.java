package com.github.zhengframework.web;

import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationAware;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.common.base.Preconditions;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.OptionalBinder;
import com.google.inject.servlet.ServletModule;
import java.util.Map;
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
    Map<String, WebConfig> configMap = ConfigurationBeanMapper
        .resolve(configuration, WebConfig.class);
    WebConfig webConfig = configMap.getOrDefault("", new WebConfig());
    bind(WebConfig.class).toInstance(webConfig);
    OptionalBinder
        .newOptionalBinder(binder(), new TypeLiteral<WebSocketEndpoint>() {
        });
    OptionalBinder.newOptionalBinder(binder(), new TypeLiteral<ServerEndpointConfig>() {
    });
    requireBinding(WebServer.class);
    bind(WebServerService.class).asEagerSingleton();
  }

}
