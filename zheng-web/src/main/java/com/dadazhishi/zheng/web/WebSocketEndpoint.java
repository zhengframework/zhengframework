/*
 * Copyright (C) 2017  Jonas Zeiger <jonas.zeiger@talpidae.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dadazhishi.zheng.web;

import java.io.IOException;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;


public abstract class WebSocketEndpoint {

  @OnOpen
  public abstract void connect(Session session) throws IOException;

  @OnMessage
  public abstract void message(String message, Session session);

  @OnMessage
  public abstract void message(byte[] data, boolean done, Session session);

  @OnError
  public abstract void error(Throwable exception, Session session);

  @OnClose
  public abstract void close(CloseReason closeReason, Session session);
}
