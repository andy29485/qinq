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

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.DefaultHandler;

import qinq.application.GameServer;

/**
 * Game
 *
 * @author az
 * @version 1.0, 2016-06-20
 *
 */
public class Game extends GameObject {
  /**
   * List of players participating in the game.
   */
  private List<Player> players;
  /**
   * The current round that is being played.
   */
  private Round        currentRound;
  /**
   * The game server that is hosting the game/
   */
  private GameServer   server;

  /**
   * @param questions
   *          that can be used to play the game
   */
  public Game(GameServer server) {
    this.players = new ArrayList<Player>();
    this.server = server;
    server.addHandle(new DefaultHandler() {
      @Override
      public void handle(String target, Request baseRequest,
          HttpServletRequest request, HttpServletResponse response) {
        // TODO handle requests
      }
    });
  }

  /**
   * Add a player to the game.
   *
   * @param strName
   *          name for the player
   * @param socket
   *          socket to connect to the player's device
   * @return whether the player has be created successfully
   */
  public boolean addPlayer(String strName, InetSocketAddress socket) {
    for (Player player : players) {
      if (player.getName().equalsIgnoreCase(strName))
        return false;
    }
    this.players.add(new Player(strName, socket));
    return true;
  }

  /**
   * Start the game
   */
  public void start(List<String> questions) {
    this.currentRound = new Round(0, "Round 1", this.players, questions);
    // TODO
  }

  /**
   * Get players that are playing this game
   *
   * @return list of players
   */
  public List<Player> getPlayers() {
    return this.players;
  }

  /**
   * Get the current round.
   *
   * @return the current round
   */
  public Round getRound() {
    return this.currentRound;
  }
}
