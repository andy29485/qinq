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

package qinq.application;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

public class GameServer {
  private Server      server;
  private HandlerList handlers;

  public GameServer() {
    this.server = new Server(8070);

    ResourceHandler resource_handler = new ResourceHandler();
    resource_handler.setDirectoriesListed(false);
    URL baseUrl = this.getClass().getResource("html");
    resource_handler.setResourceBase(baseUrl.toExternalForm());
    resource_handler.setWelcomeFiles(new String[] { "index.html" });

    handlers = new HandlerList();
    handlers.addHandler(resource_handler);
    handlers.addHandler(new DefaultHandler());
    this.server.setHandler(handlers);

    try {
      this.server.start();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void stop() {
    try {
      this.server.stop();
      this.server.join();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String getAddress() {
    if (((ServerConnector) this.server.getConnectors()[0])
        .getLocalPort() == -1) {
      return "There was an error...";
    }
    try {
      return String.format("%s:%d", InetAddress.getLocalHost().getHostAddress(),
          ((ServerConnector) this.server.getConnectors()[0]).getLocalPort());
    }
    catch (UnknownHostException e) {
      e.printStackTrace();
      return "Unknown";
    }
  }

  public void addHandle(Handler handler) {
    // handlers.addHandler(resource_handler);

  }
}
