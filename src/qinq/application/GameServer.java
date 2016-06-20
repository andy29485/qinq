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

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

public class GameServer {
  public GameServer() {
    Server server = new Server(8070);

    ResourceHandler resource_handler = new ResourceHandler();
    resource_handler.setDirectoriesListed(false);
    resource_handler.setWelcomeFiles(new String[] { "index.html" });

    resource_handler.setResourceBase(".");

    HandlerList handlers = new HandlerList();
    handlers
        .setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
    server.setHandler(handlers);

    try {
      server.start();
      server.join();
    }
    catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public String getAddress() {
    // TODO
    return "";
  }
}
