package com.github.zhengframework.web;

import com.github.zhengframework.configuration.ConfigurationAwareServletModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.OptionalBinder;
import java.util.Map;
import javax.websocket.server.ServerEndpointConfig;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@EqualsAndHashCode(callSuper = false, of = {})
public class WebModule extends ConfigurationAwareServletModule {

  @Override
  protected void configureServlets() {
    Map<String, WebConfig> configMap = ConfigurationBeanMapper
        .resolve(getConfiguration(), WebConfig.class);
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
