package com.github.zhengframework.web.jetty;

/*-
 * #%L
 * zheng-web-jetty
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
