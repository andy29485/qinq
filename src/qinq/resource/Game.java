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

import java.util.ArrayList;
import java.util.List;

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
   * @param questions
   *          that can be used to play the game
   */
  public Game() {
    this.players = new ArrayList<Player>();
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
  public int addPlayer(String strName) {
    for (Player player : this.players) {
      if (player.getName().equalsIgnoreCase(strName))
        return -1;
    }
    Player p = new Player(strName);
    this.players.add(p);
    return p.getID();
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
   * Get a player by name.
   *
   * @param name
   *          the name of the player to find(case insensitive).
   * @return the player object, or null if player was not found.
   */
  public Player getPlayerByName(String name) {
    if (this.players == null)
      return null;
    for (Player p : this.players) {
      if (p.getName().equalsIgnoreCase(name))
        return p;
    }
    return null;
  }

  /**
   * Get a player by id.
   *
   * @param id
   *          the id of the player to find.
   * @return the player object, or null if player was not found.
   */
  public Player getPlayerById(int id) {
    if (this.players == null)
      return null;
    for (Player p : this.players) {
      if (p.getID() == id)
        return p;
    }
    return null;
  }

  /**
   * Get the current round.
   *
   * @return the current round
   */
  public Round getRound() {
    return this.currentRound;
  }

  /**
   * Get a answer by id.
   *
   * @param id
   *          the id of the answer to find.
   * @return the answer object, or null if answer was not found.
   */
  public Answer getAnswerById(int id) {
    if (this.currentRound == null)
      return null;
    for (Answer a : this.currentRound.getAnswers()) {
      if (a.getID() == id)
        return a;
    }
    return null;
  }

  /**
   * Get a question by id.
   *
   * @param id
   *          the id of the question to find.
   * @return the question object, or null if question was not found.
   */
  public Question getQuestionById(int id) {
    if (this.currentRound == null)
      return null;
    for (Question q : this.currentRound.getQuestions()) {
      if (q.getID() == id)
        return q;
    }
    return null;
  }
}
