package com.github.zhengframework.web.jetty;

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.web.GuiceServerEndpointConfigurator;
import com.github.zhengframework.web.WebModule;
import com.github.zhengframework.web.WebServer;
import com.github.zhengframework.web.WebSocketEndpoint;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.multibindings.OptionalBinder;
import javax.websocket.server.ServerEndpointConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class JettyWebModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    WebModule webModule = new WebModule();
    webModule.initConfiguration(getConfiguration());
    install(webModule);
    // web socket
    bind(GuiceServerEndpointConfigurator.class);
    Multibinder.newSetBinder(binder(), ServerEndpointConfig.class);
    Multibinder.newSetBinder(binder(), new TypeLiteral<Class<? extends WebSocketEndpoint>>() {
    });

    OptionalBinder.newOptionalBinder(binder(), Server.class).setDefault().toProvider(
        () -> {
          QueuedThreadPool threadPool = new QueuedThreadPool();
          threadPool.setMaxThreads(500);
          return new Server(threadPool);
        });
    OptionalBinder.newOptionalBinder(binder(), JettyServerConfigurer.class)
        .setDefault().to(DefaultJettyServerConfigurer.class);

    bind(WebServer.class).to(JettyWebServer.class);
  }

}
