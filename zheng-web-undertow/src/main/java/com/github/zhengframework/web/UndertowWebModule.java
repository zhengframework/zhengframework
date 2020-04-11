package com.github.zhengframework.web;

import com.github.zhengframework.core.Configuration;
import com.github.zhengframework.core.ConfigurationAware;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.multibindings.OptionalBinder;
import io.undertow.Undertow;
import io.undertow.servlet.api.ClassIntrospecter;
import javax.websocket.server.ServerEndpointConfig;

public class UndertowWebModule extends AbstractModule implements ConfigurationAware {

  private Configuration configuration;

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  protected void configure() {
    WebModule webModule = new WebModule();
    webModule.initConfiguration(configuration);
    install(webModule);
    // WebSocket
    Multibinder.newSetBinder(binder(), ServerEndpointConfig.class);
    Multibinder.newSetBinder(binder(), new TypeLiteral<Class<? extends WebSocketEndpoint>>() {
    });

    bind(ClassIntrospecter.class).to(GuiceClassIntrospecter.class);
    bind(GuiceServerEndpointConfigurator.class);

    OptionalBinder.newOptionalBinder(binder(), Undertow.Builder.class)
        .setDefault().toProvider(UndertowServerProvider.class);

    OptionalBinder.newOptionalBinder(binder(), UndertowServerConfigurer.class)
        .setDefault().to(DefaultUndertowServerConfigurer.class);

    bind(WebServer.class).to(UndertowWebServer.class);
  }

}
