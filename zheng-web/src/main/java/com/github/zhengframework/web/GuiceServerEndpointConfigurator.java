package com.github.zhengframework.web;

import com.google.inject.Injector;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.websocket.Extension;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class GuiceServerEndpointConfigurator extends ServerEndpointConfig.Configurator {

  private final Injector injector;

  @Inject
  public GuiceServerEndpointConfigurator(Injector injector) {
    this.injector = injector;
  }

  @Override
  public String getNegotiatedSubprotocol(final List<String> supported,
      final List<String> requested) {
    for (String proto : requested) {
      if (supported.contains(proto)) {
        return proto;
      }
    }
    return "";
  }

  @Override
  public List<Extension> getNegotiatedExtensions(final List<Extension> installed,
      final List<Extension> requested) {
    final List<Extension> ret = new ArrayList<>();
    for (Extension req : requested) {
      for (Extension extension : installed) {
        if (extension.getName().equals(req.getName())) {
          ret.add(req);
          break;
        }
      }
    }
    return ret;
  }

  @Override
  public boolean checkOrigin(final String originHeaderValue) {
    //we can't actually do anything here, because have have absolutely no context.
    return true;
  }

  @Override
  public void modifyHandshake(final ServerEndpointConfig sec, final HandshakeRequest request,
      final HandshakeResponse response) {
  }

  @Override
  public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
    return injector.getInstance(endpointClass);
  }
}
