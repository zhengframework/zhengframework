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

  @Inject private GuiceServerEndpointConfigurator configurator;
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
