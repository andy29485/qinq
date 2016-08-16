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
import org.json.JSONException;
import org.json.JSONObject;

import qinq.application.GamePane;

public class QinqWebSocketAddapter extends WebSocketAdapter {
  private Session session;
  private Game    game;
  private Player  player;

  /**
   * Create a socket attached to the game
   *
   * @param game
   *          pertaining to this socket
   */
  public QinqWebSocketAddapter(Game game) {
    this.game = game;
  }

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
    this.session = session;
  }

  @Override
  public void onWebSocketText(String message) {
    JSONObject jsonOut = new JSONObject();
    JSONObject json;

    try {
      json = new JSONObject(message);
    }
    catch (JSONException e) {
      e.printStackTrace();
      return;
    }

    switch (json.getString("action").toLowerCase()) {
      case "create user":
        this.createUser(json, jsonOut);
        break;
      case "send answer":
        this.answer(json);
        return;
      case "vote":
        if (this.vote(json, jsonOut))
          break;
        else
          return;
      default:
        return;
    }

    this.sendText(jsonOut.toString());

  }

  /**
   * processes a vote user sent in for a question
   *
   * @param json
   *          question id to vote for
   * @param jsonOut
   *          votes left and stuff
   * @return true if there is info to send back
   */
  private boolean vote(JSONObject json, JSONObject jsonOut) {
    int aid = Integer.valueOf(json.getString("aid"));
    Answer answer = this.game.getAnswerById(aid);
    if (answer != null && this.player != null) {
      jsonOut.put("action", "voting");
      jsonOut.put("aid", aid);
      jsonOut.put("voted", answer.vote(this.player));
      jsonOut.put("left", this.player.getVotes());
      return true;
    }
    return false;
  }

  /**
   * User sends in an answer to a question, this method processes it
   *
   * @param json
   *          answer and question id from user
   */
  private void answer(JSONObject json) {
    int id = Integer.valueOf(json.getString("id"));
    Answer answer = this.game.getAnswerById(id);
    if (answer == null)
      return;
    answer.setAnswer(json.getString("answer"));
    answer.getPlayer().getAnswers().remove(answer);

    GamePane display = this.game.getRound().getDisplay();
    for (Player p : this.game.getPlayers())
      p.getSocket().sendText(display.getJson().toString());
    for (Player p : this.game.getSpectators())
      p.getSocket().sendText(display.getJson().toString());

    if (answer.getPlayer().getAnswers().size() > 0) {
      Round round = this.game.getRound();
      int time = (round == null) ? 0 : round.getTime();
      answer.getPlayer().getAnswers().get(0).send(time);
    }
  }

  /**
   * Connect user to the game(user *may* already exist, if not will be created)
   *
   * @param json
   *          info coming from user
   * @param jsonOut
   *          info sent back to the user
   */
  private void createUser(JSONObject json, JSONObject jsonOut) {
    this.player = this.game.addPlayer(json.getString("name").toUpperCase(),
        this.session.getRemoteAddress().getAddress().getHostAddress());
    jsonOut.put("action", "creating");
    if (this.player != null) {
      jsonOut.put("created", "true");
      jsonOut.put("color", this.player.getColor());
      jsonOut.put("name", this.player.getName().toUpperCase());
      jsonOut.put("id", this.player.getID());
      this.player.setSocket(this);
    }
    else {
      jsonOut.put("created", "false");
    }
  }

  /**
   * Send a string over this socket
   *
   * @param message
   *          string to send
   * @return true on success
   */
  public boolean sendText(String message) {
    if (this.session == null)
      return false;
    try {
      this.session.getRemote().sendString(message);
      return true;
    }
    catch (IOException e) {
      return false;
    }
  }
}
