package com.github.zhengframework.web;

/*-
 * #%L
 * zheng-web
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
