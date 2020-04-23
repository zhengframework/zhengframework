package com.github.zhengframework.web;

import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationAware;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.multibindings.OptionalBinder;
import io.undertow.Undertow;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.ResourceManager;
import io.undertow.servlet.api.ClassIntrospecter;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.websocket.server.ServerEndpointConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    OptionalBinder.newOptionalBinder(binder(), ResourceManager.class)
        .setDefault().toProvider((Provider<ResourceManager>) () -> {
      log.info("Configuring Resources to be found in META-INF/resources/");
      ClassPathResourceManager classPathResourceManager = new ClassPathResourceManager(
          Thread.currentThread().getContextClassLoader(), "META-INF/resources/");
      return new ResourceManagerCollection(classPathResourceManager);
    }).in(Singleton.class);

    bind(WebServer.class).to(UndertowWebServer.class);
  }

}
