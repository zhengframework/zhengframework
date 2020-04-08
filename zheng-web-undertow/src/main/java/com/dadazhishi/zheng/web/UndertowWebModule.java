package com.dadazhishi.zheng.web;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import io.undertow.servlet.api.ClassIntrospecter;
import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.util.ImmediateInstanceFactory;
import io.undertow.websockets.jsr.DefaultContainerConfigurator;
import java.util.Optional;
import javax.annotation.Nullable;
import javax.inject.Singleton;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

public class UndertowWebModule extends WebModule {

  @Override
  protected void configureServlets() {
    super.configureServlets();
    bind(ClassIntrospecter.class).to(GuiceClassIntrospecter.class);
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  @Provides
  @Nullable
  @Singleton
  public Optional<Configurator> provideDefaultServerEndpointConfigurator(
      Optional<ServerEndpointConfig> serverEndpointConfig, Injector injector) {
    return serverEndpointConfig
        .map(endpointConfig -> new GuiceServerEndpointConfigurator(injector));
  }

  private static class GuiceServerEndpointConfigurator extends DefaultContainerConfigurator {

    private final Injector injector;

    GuiceServerEndpointConfigurator(Injector injector) {
      this.injector = injector;
    }

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
      return injector.getInstance(endpointClass);
    }
  }

  private static class GuiceClassIntrospecter implements ClassIntrospecter {

    private final Injector injector;


    @Inject
    public GuiceClassIntrospecter(Injector injector) {
      this.injector = injector;
    }

    @Override
    public <T> InstanceFactory<T> createInstanceFactory(Class<T> clazz)
        throws NoSuchMethodException {
      return new ImmediateInstanceFactory<>(injector.getInstance(clazz));
    }
  }
}
