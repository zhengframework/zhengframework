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

import com.github.zhengframework.web.WebServer;
import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UndertowWebServer implements WebServer {

  private final Undertow.Builder server;
  private final UndertowServerConfigurer undertowServerConfigurer;
  private Undertow undertow;

  @Inject
  public UndertowWebServer(Builder server, UndertowServerConfigurer undertowServerConfigurer) {
    this.server = server;
    this.undertowServerConfigurer = undertowServerConfigurer;
  }

  @Override
  public void start() throws Exception {
    undertowServerConfigurer.configure(server);
    undertow = server.build();
    undertow.start();
  }

  @Override
  public void stop() throws Exception {
    undertow.stop();
  }
}
