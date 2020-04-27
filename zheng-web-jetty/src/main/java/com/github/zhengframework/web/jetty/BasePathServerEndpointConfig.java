package com.github.zhengframework.web.jetty;

import com.github.zhengframework.web.PathUtils;
import java.util.List;
import java.util.Map;
import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.Extension;
import javax.websocket.server.ServerEndpointConfig;

public class BasePathServerEndpointConfig implements ServerEndpointConfig {

  private final String basePath;
  private final ServerEndpointConfig delegate;

  public BasePathServerEndpointConfig(String basePath, ServerEndpointConfig delegate) {
    this.basePath = basePath;
    this.delegate = delegate;
  }

  @Override
  public Class<?> getEndpointClass() {
    return delegate.getEndpointClass();
  }

  @Override
  public String getPath() {
    return PathUtils.fixPath(basePath, delegate.getPath());
  }

  @Override
  public List<String> getSubprotocols() {
    return delegate.getSubprotocols();
  }

  @Override
  public List<Extension> getExtensions() {
    return delegate.getExtensions();
  }

  @Override
  public Configurator getConfigurator() {
    return delegate.getConfigurator();
  }

  @Override
  public List<Class<? extends Encoder>> getEncoders() {
    return delegate.getEncoders();
  }

  @Override
  public List<Class<? extends Decoder>> getDecoders() {
    return delegate.getDecoders();
  }

  @Override
  public Map<String, Object> getUserProperties() {
    return delegate.getUserProperties();
  }
}
