package com.github.zhengframework.web;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.Extension;
import javax.websocket.server.ServerEndpointConfig;

public class BasicServerEndpointConfig implements ServerEndpointConfig {

  @Inject
  private GuiceServerEndpointConfigurator configurator;
  private String path;
  private Class<?> endpointClass;

  public BasicServerEndpointConfig(Class<?> endpointClass, String path) {
    this.endpointClass = endpointClass;
    this.path = path;
  }

  @Override
  public Class<?> getEndpointClass() {
    return endpointClass;
  }

  @Override
  public String getPath() {
    return path;
  }

  @Override
  public List<String> getSubprotocols() {
    return Collections.emptyList();
  }

  @Override
  public List<Extension> getExtensions() {
    return Collections.emptyList();
  }

  @Override
  public Configurator getConfigurator() {
    return configurator;
  }

  @Override
  public List<Class<? extends Encoder>> getEncoders() {
    return Collections.emptyList();
  }

  @Override
  public List<Class<? extends Decoder>> getDecoders() {
    return Collections.emptyList();
  }

  @Override
  public Map<String, Object> getUserProperties() {
    return new HashMap<>();
  }
}
