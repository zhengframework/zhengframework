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
import com.github.zhengframework.web.WebModule;
import com.github.zhengframework.web.WebServer;
import com.google.inject.multibindings.OptionalBinder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class JettyWebModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    WebModule webModule = new WebModule();
    webModule.initConfiguration(getConfiguration());
    install(webModule);

    bindJettyServer();
    bindJettyServerConfigurer();
    bindWebServer();
  }

  protected void bindJettyServerConfigurer() {
    OptionalBinder.newOptionalBinder(binder(), JettyServerConfigurer.class)
        .setDefault()
        .to(DefaultJettyServerConfigurer.class);
  }

  protected void bindWebServer() {
    bind(WebServer.class).to(JettyWebServer.class);
  }

  protected void bindJettyServer() {
    OptionalBinder.newOptionalBinder(binder(), Server.class)
        .setDefault()
        .toProvider(
            () -> {
              QueuedThreadPool threadPool = new QueuedThreadPool();
              threadPool.setMaxThreads(500);
              return new Server(threadPool);
            });
  }
}
