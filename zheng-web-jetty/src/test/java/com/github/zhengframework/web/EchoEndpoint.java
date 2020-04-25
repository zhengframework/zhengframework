package com.github.zhengframework.web;

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
  public void connect(Session session) throws IOException {

  }

  @SneakyThrows
  @Override
  public void message(String message, Session session) {
    System.out.println();
    log.info("onMessage={}", message);
    session.getBasicRemote().sendText(message);
  }

  @Override
  public void message(byte[] data, boolean done, Session session) {

  }

  @Override
  public void error(Throwable exception, Session session) {

  }

  @Override
  public void close(CloseReason closeReason, Session session) {

  }
}
