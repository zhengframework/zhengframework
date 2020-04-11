package com.github.zhengframework.web;

import com.github.zhengframework.core.Configuration;
import com.github.zhengframework.core.ConfigurationAware;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.multibindings.OptionalBinder;
import javax.websocket.server.ServerEndpointConfig;
import org.eclipse.jetty.server.Server;

public class JettyWebModule extends AbstractModule implements ConfigurationAware {

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
    OptionalBinder.newOptionalBinder(binder(), Server.class).setDefault().toProvider(Server::new);
    OptionalBinder.newOptionalBinder(binder(), JettyServerConfigurer.class)
        .setDefault().to(DefaultJettyServerConfigurer.class);
    Multibinder.newSetBinder(binder(), ServerEndpointConfig.class);
    Multibinder.newSetBinder(binder(), WebSocketEndpoint.class);
    bind(WebServer.class).to(JettyServer.class);
  }

}
