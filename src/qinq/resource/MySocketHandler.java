/*
 * Copyright (c) 2016, Andriy Zasypkin.
 *
 * This file is part of Qinq.
 *
 * Qinq(or QINQ) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Qinq in distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Qinq. If not, see <http://www.gnu.org/licenses/>.
 */

package qinq.resource;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class MySocketHandler extends WebSocketHandler {
  @Override
  public void configure(WebSocketServletFactory factory) {
    factory.setCreator(new QinqWebSocketCreator());
  }

  private class QinqWebSocketCreator implements WebSocketCreator {
    public Object createWebSocket(ServletUpgradeRequest request,
        ServletUpgradeResponse response) {
      return new QinqWebSocketAddapter();
    }
  }

  private class QinqWebSocketAddapter extends WebSocketAdapter {
    @Override
    public void onWebSocketClose(int statusCode, String reason) {
      System.out
          .println("Close: statusCode=" + statusCode + ", reason=" + reason);
    }

    @Override
    public void onWebSocketError(Throwable t) {
      System.out.println("Error: " + t.getMessage());
    }

    @Override
    public void onWebSocketConnect(Session session) {
      System.out.println("Connect: " + session.getRemoteAddress().getAddress());
      try {
        session.getRemote().sendString("Hello Webbrowser");
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    public void onWebSocketText(String message) {
      System.out.println("Message: " + message);
    }
  }
}
