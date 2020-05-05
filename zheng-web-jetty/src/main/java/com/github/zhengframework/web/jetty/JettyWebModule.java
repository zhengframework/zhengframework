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

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.web.GuiceServerEndpointConfigurator;
import com.github.zhengframework.web.WebModule;
import com.github.zhengframework.web.WebServer;
import com.github.zhengframework.web.WebSocketEndpoint;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.multibindings.OptionalBinder;
import javax.websocket.server.ServerEndpointConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class JettyWebModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    WebModule webModule = new WebModule();
    webModule.initConfiguration(getConfiguration());
    install(webModule);
    // web socket
    bind(GuiceServerEndpointConfigurator.class);
    Multibinder.newSetBinder(binder(), ServerEndpointConfig.class);
    Multibinder.newSetBinder(binder(), new TypeLiteral<Class<? extends WebSocketEndpoint>>() {
    });

    OptionalBinder.newOptionalBinder(binder(), Server.class).setDefault().toProvider(
        () -> {
          QueuedThreadPool threadPool = new QueuedThreadPool();
          threadPool.setMaxThreads(500);
          return new Server(threadPool);
        });
    OptionalBinder.newOptionalBinder(binder(), JettyServerConfigurer.class)
        .setDefault().to(DefaultJettyServerConfigurer.class);

    bind(WebServer.class).to(JettyWebServer.class);
  }

}
