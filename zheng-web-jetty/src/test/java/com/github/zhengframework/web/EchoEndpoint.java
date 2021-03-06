package com.github.zhengframework.web;

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

import java.io.IOException;
import javax.websocket.CloseReason;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ServerEndpoint("/echo")
public class EchoEndpoint extends WebSocketEndpoint {

  @Override
  public void connect(Session session) throws IOException {}

  @SneakyThrows
  @Override
  public void message(String message, Session session) {
    log.info("onMessage={}", message);
    session.getBasicRemote().sendText(message);
  }

  @Override
  public void message(byte[] data, boolean done, Session session) {}

  @Override
  public void error(Throwable exception, Session session) {}

  @Override
  public void close(CloseReason closeReason, Session session) {}
}
