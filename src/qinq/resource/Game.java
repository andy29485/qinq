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
import java.util.stream.Collectors;

import qinq.application.GamePane;
import qinq.application.GameUI;

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
   * The Game UI, needed for refreshing people, and other such tasks(probably).
   */
  private GameUI       gameui;

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
   * @param ip
   *          used to connect/reconnect
   * @return whether the player has be created successfully
   */
  public synchronized int addPlayer(String strName, String ip) {
    for (Player player : this.players) {
      if (player.getName().equalsIgnoreCase(strName)) {
        if (player.getIp().equalsIgnoreCase(ip))
          return player.getID();
        else
          return -1;
      }
    }
    if (this.currentRound != null) // Too late to create a player, a game has
                                   // already started
      return -2;
    Player p = new Player(strName, ip);
    this.players.add(p);
    if (this.gameui != null)
      this.gameui.addPlayer(p);
    return p.getID();
  }

  /**
   * Start the game
   *
   * @param questions
   *          a list of questions that will be used for the current game
   * @param display
   *          the pane on which to display result on
   */
  public synchronized void start(List<String> questions, GamePane display) {
    if (this.currentRound != null || this.players.size() < 3)
      return;

    // Remove duplicate questions
    List<String> nonDupQuestions = questions.parallelStream()
        .map(String::toUpperCase).distinct().collect(Collectors.toList());

    if (questions.size() < this.players.size() * 2 + 1)// 2 regular rounds + 1
                                                       // final round requires
                                                       // 2n+1 questions where n
                                                       // is the number of
                                                       // players
      return;
    new Thread() {
      @Override
      public void run() {
        Game.this.currentRound = new Round(0, "Round 1", Game.this.players,
            nonDupQuestions, display);
        Game.this.currentRound.answer();
        Game.this.currentRound.vote();
        Game.this.currentRound.saveResults();

        Game.this.currentRound = new Round(0, "Round 2", Game.this.players,
            nonDupQuestions, display);
        Game.this.currentRound.answer();
        Game.this.currentRound.vote();
        Game.this.currentRound.saveResults();

        Game.this.currentRound = new Round(1, "Final Round", Game.this.players,
            nonDupQuestions, display);
        Game.this.currentRound.answer();
        Game.this.currentRound.vote();
        Game.this.currentRound.saveResults();

        Game.this.currentRound = null; // End the game
      }
    }.start();
  }

  /**
   * Get players that are playing this game
   *
   * @return list of players
   */
  public synchronized List<Player> getPlayers() {
    return this.players;
  }

  /**
   * Get a player by name.
   *
   * @param name
   *          the name of the player to find(case insensitive).
   * @return the player object, or null if player was not found.
   */
  public synchronized Player getPlayerByName(String name) {
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
  public synchronized Player getPlayerById(int id) {
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
  public synchronized Round getRound() {
    return this.currentRound;
  }

  /**
   * Get a answer by id.
   *
   * @param id
   *          the id of the answer to find.
   * @return the answer object, or null if answer was not found.
   */
  public synchronized Answer getAnswerById(int id) {
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
  public synchronized Question getQuestionById(int id) {
    if (this.currentRound == null)
      return null;
    for (Question q : this.currentRound.getQuestions()) {
      if (q.getID() == id)
        return q;
    }
    return null;
  }

  /**
   * Set the Game UI
   *
   * @param gameui
   *          the game ui to set
   */
  public synchronized void setGameUI(GameUI gameui) {
    this.gameui = gameui;
  }
}
