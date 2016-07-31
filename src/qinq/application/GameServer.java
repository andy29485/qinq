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

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.json.JSONArray;
import org.json.JSONObject;

import qinq.resource.Answer;
import qinq.resource.Game;
import qinq.resource.Player;
import qinq.resource.Round;

/**
 * Class that is responsible for managing the web interface of the game.
 *
 * @author az
 * @version 1.0, 2016-06-22
 *
 */
public class GameServer {
  private Server server;
  private Game   game;

  /**
   * Create an instance of the game server to use
   */
  public GameServer() {
    this.server = new Server(8070);
    // this.server.addConnector(new ServerConnector(this.server));

    ResourceHandler resource_handler = new ResourceHandler();
    resource_handler.setDirectoriesListed(true);
    try {
      URL webRootLocation =
          this.getClass().getResource("/qinq/application/html/index.html");
      URI webRootUri = URI.create(webRootLocation.toURI().toASCIIString()
          .replaceFirst("/index.html$", "/"));
      resource_handler.setBaseResource(Resource.newResource(webRootUri));
      resource_handler.setWelcomeFiles(new String[] { "index.html" });
    }
    catch (MalformedURLException e) {
      e.printStackTrace();
    }
    catch (URISyntaxException e) {
      e.printStackTrace();
    }
    HandlerList handlers = new HandlerList();
    handlers.setHandlers(new Handler[] { new MyHandler(), resource_handler });
    this.server.setHandler(handlers);

    try {
      this.server.start();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Stop server from hosting
   */
  public void stop() {
    try {
      this.server.stop();
      this.server.join();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Get the IP address and port to join the game
   *
   * @return string containing the address/port of the website part of the game
   */
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

  /**
   * Attach the game instance to the server
   *
   * @param game,
   *          the game associated with this server
   */
  public void setGame(Game game) {
    this.game = game;
  }

  /**
   * Class that handles what happens when a request is sent.
   *
   * For this particular thing, I assume that an ajax post, containing json data
   * is sent.
   *
   */
  class MyHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
      Game g = GameServer.this.game;
      if (g == null || !target.equalsIgnoreCase("/data.json"))
        return;

      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = request.getReader().readLine()) != null) {
        sb.append(line);
      }

      JSONObject json;
      try {
        json = new JSONObject(sb.toString());
      }
      catch (Exception e) {
        return;
      }

      int id;
      int aid;
      double time;
      Answer a;
      Player p;
      Round round = GameServer.this.game.getRound();

      JSONObject jsonOut = new JSONObject();

      switch (json.getString("action").toLowerCase()) {
        case "create user":
          p = g.addPlayer(json.getString("name").toUpperCase(),
              request.getRemoteAddr());
          if (p != null) {
            jsonOut.put("created", "true");
            jsonOut.put("color", p.getColor());
            jsonOut.put("name", p.getName().toUpperCase());
            jsonOut.put("id", p.getID());
          }
          else {
            jsonOut.put("created", "false");
          }
          break;
        case "send answer":
          id = Integer.valueOf(json.getString("id"));
          a = g.getAnswerById(id);
          if (a == null)
            return;
          a.setAnswer(json.getString("answer"));
          a.getPlayer().getAnswers().remove(a);
          break;
        case "vote":
          id = Integer.valueOf(json.getString("id"));
          aid = Integer.valueOf(json.getString("aid"));
          p = g.getPlayerById(id);
          a = g.getAnswerById(aid);
          if (a != null && p != null) {
            jsonOut.put("voted", a.vote(p));
            jsonOut.put("left", p.getVotes());
          }
          else
            return;
          break;
        case "get state":
          id = Integer.valueOf(json.getString("id"));
          p = g.getPlayerById(id);
          if (p == null) {
            jsonOut.put("action", "die");
            break;
          }

          jsonOut.put("score", p.getPoints());

          if (g.getRound() != null)
            time = g.getRound().getTime();
          else
            time = 0;
          if (json.getString("state").equalsIgnoreCase("waiting")) {
            if (p.getAnswers().size() > 0 && round != null
                && round.getQuestion() == null) {
              a = p.getAnswers().get(0);
              jsonOut.put("action", "answer");
              jsonOut.put("time", time);
              jsonOut.put("aid", a.getID());
              jsonOut.put("question", a.getQuestion());
            }
            else if (round != null && round.getQuestion() != null
                && p.getVotes() > 0) {
              jsonOut.put("action", "vote");
              jsonOut.put("time", time);
              jsonOut.put("question", g.getRound().getQuestion().getQuestion());
              jsonOut.put("votes", p.getVotes());
              JSONArray jSONArray = new JSONArray();
              for (Answer tmp : g.getRound().getQuestion().getAnswers()) {
                if (tmp.getPlayer() != p && !tmp.getAnswer().isEmpty())
                  jSONArray.put(new JSONObject().put("answer", tmp.getAnswer())
                      .put("aid", tmp.getID()));
              }
              jsonOut.put("answers", jSONArray);
            }
            else {
              jsonOut.put("action", "wait");
              jsonOut.put("time", time);
            }
          }
          else {
            jsonOut.put("action", "nothing");
            jsonOut.put("time", time);
          }
          if (round != null) {
            jsonOut.put("info", round.getDisplay().getJson());
          }
          else {
            jsonOut.put("info", "none");
          }
          break;
        default:
          return;
      }

      response.setContentType("application/json");
      response.setHeader("Cache-Control", "no-cache");
      response.setContentLength(jsonOut.toString().length());
      response.setStatus(HttpServletResponse.SC_OK);
      response.getWriter().println(jsonOut.toString());
      response.flushBuffer();
      baseRequest.setHandled(true);
    }
  }
}
