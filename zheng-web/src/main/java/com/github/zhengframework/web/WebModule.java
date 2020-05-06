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

import com.github.zhengframework.configuration.ConfigurationAwareServletModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.OptionalBinder;
import java.util.Map;
import javax.websocket.server.ServerEndpointConfig;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(
    callSuper = false,
    of = {})
public class WebModule extends ConfigurationAwareServletModule {

  @Override
  protected void configureServlets() {
    Map<String, WebConfig> configMap =
        ConfigurationBeanMapper.resolve(getConfiguration(), WebConfig.class);
    WebConfig webConfig = configMap.getOrDefault("", new WebConfig());
    bind(WebConfig.class).toInstance(webConfig);
    OptionalBinder.newOptionalBinder(binder(), new TypeLiteral<WebSocketEndpoint>() {});
    OptionalBinder.newOptionalBinder(binder(), new TypeLiteral<ServerEndpointConfig>() {});
    requireBinding(WebServer.class);
    bind(WebServerService.class).asEagerSingleton();
  }
}
