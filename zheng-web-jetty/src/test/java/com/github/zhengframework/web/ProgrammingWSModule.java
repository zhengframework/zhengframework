package com.github.zhengframework.web;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import javax.websocket.server.ServerEndpointConfig;

public class ProgrammingWSModule extends AbstractModule {

  protected void configure() {
    String echoPath = "/echo222";
    BasicServerEndpointConfig serverEndpointConfig = new BasicServerEndpointConfig(
        EchoEndpoint.class, echoPath);
    requestInjection(serverEndpointConfig);
    Multibinder.newSetBinder(binder(), ServerEndpointConfig.class)
        .addBinding().toInstance(serverEndpointConfig);
  }
}
