package com.github.zhengframework.web.undertow;

/*-
 * #%L
 * zheng-web-undertow
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

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.web.GuiceServerEndpointConfigurator;
import com.github.zhengframework.web.WebModule;
import com.github.zhengframework.web.WebServer;
import com.github.zhengframework.web.WebSocketEndpoint;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.multibindings.OptionalBinder;
import io.undertow.Undertow;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.ResourceManager;
import io.undertow.servlet.api.ClassIntrospecter;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.websocket.server.ServerEndpointConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UndertowWebModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    WebModule webModule = new WebModule();
    webModule.initConfiguration(getConfiguration());
    install(webModule);
    // WebSocket
    Multibinder.newSetBinder(binder(), ServerEndpointConfig.class);
    Multibinder.newSetBinder(binder(), new TypeLiteral<Class<? extends WebSocketEndpoint>>() {
    });

    bind(ClassIntrospecter.class).to(GuiceClassIntrospecter.class);
    bind(GuiceServerEndpointConfigurator.class);

    OptionalBinder.newOptionalBinder(binder(), Undertow.Builder.class)
        .setDefault()
        .toProvider(UndertowServerProvider.class);

    OptionalBinder.newOptionalBinder(binder(), UndertowServerConfigurer.class)
        .setDefault()
        .to(DefaultUndertowServerConfigurer.class);

    OptionalBinder.newOptionalBinder(binder(), ResourceManager.class)
        .setDefault()
        .toProvider(
            (Provider<ResourceManager>)
                () -> {
                  log.info("Configuring Resources to be found in META-INF/resources/");
                  ClassPathResourceManager classPathResourceManager =
                      new ClassPathResourceManager(
                          Thread.currentThread().getContextClassLoader(), "META-INF/resources/");
                  return new ResourceManagerCollection(classPathResourceManager);
                })
        .in(Singleton.class);

    bind(WebServer.class).to(UndertowWebServer.class);
  }
}
