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
import java.util.Collections;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Label;

/**
 * Game
 *
 * @author az
 * @version 1.0, 2016-06-20
 *
 */
public class Player extends GameObject {
  /**
   * The nick name the player will go by during the game
   */
  private String                   strName;
  /**
   * The IP address used to connect, primarily used to reconnect
   */
  private String                   strIP;
  /**
   * List of Answers for the current Game/Round
   */
  private List<Answer>             answers;
  /**
   * Number of votes a player has.
   */
  private int                      nVotes;
  /**
   * Number of points a player has.
   */
  private int                      nPoints;
  /**
   * Colour of the player.
   */
  private String                   color;

  /**
   * WebSocket used to interact with the players web interface
   */
  private QinqWebSocketAddapter    socket;
  /**
   * Total number of players, use for generating player IDs.
   */
  private static int               nPlayers = 0;
  /**
   * Colours that can be used to assign a player.
   */
  @SuppressWarnings("serial")
  public static final List<String> COLOURS  =
      Collections.unmodifiableList(new ArrayList<String>() {
                                                  {
                                                    add("#ff6666");
                                                    add("#66ff99");
                                                    add("#3366ff");
                                                    add("#b100ec");
                                                    add("#38e0ff");
                                                    add("#ffcc66");
                                                    add("#cc6600");
                                                    add("#f88fe8");
                                                    add("#009999");
                                                    add("#cccc00");
                                                  }
                                                });

  /**
   * create a new player from a name and an IP
   *
   * @param strName
   *          nick name of the player
   * @param strIP
   *          player's IP address
   */
  public Player(String strName, String strIP) {
    super(strName.isEmpty() ? -++Player.nPlayers : ++Player.nPlayers);
    this.answers = new ArrayList<Answer>();
    this.strName = strName.isEmpty() ? "Spectator" : strName;
    this.strIP = strIP;
    this.nPoints = 0;
    this.setVotes(0);
    if (strName.isEmpty())
      this.color = "#555555";
    else
      this.color = Player.COLOURS.get(this.getID() % Player.COLOURS.size());
  }

  /**
   * Get player name
   *
   * @return the strName
   */
  public String getName() {
    return this.strName.toUpperCase();
  }

  /**
   * Set player name
   *
   * @param strName
   *          the strName to set
   */
  public void setName(String strName) {
    this.strName = strName.toUpperCase();
  }

  /**
   * Get the answers a player still has to answer
   *
   * @return the answers
   */
  public List<Answer> getAnswers() {
    return this.answers;
  }

  /**
   * Get the number of votes a player still has
   *
   * @return the number of votes
   */
  public int getVotes() {
    return nVotes;
  }

  /**
   * Set the number of votes a player has
   *
   * @param nVotes
   *          the new number of votes to set
   */
  public void setVotes(int nVotes) {
    this.nVotes = nVotes;
  }

  /**
   * Use a player's vote (decrease num votes by one)
   *
   */
  public void useVote() {
    this.nVotes--;
  }

  /**
   * Get the player's IP address
   *
   * @return the IP address to set
   */
  public String getIp() {
    return this.strIP;
  }

  /**
   * Set the player's IP address
   *
   * @param strIP
   *          the IP address to set
   */
  public void setIp(String strIP) {
    this.strIP = strIP;
  }

  /**
   * Get the player's colour
   *
   * @return the colour
   */
  public String getColor() {
    return this.color;
  }

  /**
   * Get the player's label(TODO: with picture)
   *
   * @return the label of the player
   */
  public Node getLargeLabel() {
    // TODO make this look more complete
    return this.getNameLabel();
  }

  /**
   * Get the player's label(Just the name with colour)
   *
   * @return the label of the player
   */
  public Node getNameLabel() {
    return this.getNameLabel("");
  }

  /**
   * Get the player's label(Just the name with colour) with additional message
   *
   * @return the label of the player
   */
  public Node getNameLabel(String message) {
    Node label;
    if (!message.isEmpty())
      label = new Label(String.format("%s - %s", this.getName(), message));
    else
      label = new Label(this.getName());
    label.setStyle(String.format("-fx-background-color: %s;", this.getColor()));
    label.getStyleClass().add("player-label");
    return label;
  }

  /**
   * Get the player's score
   *
   * @return the the player's score
   */
  public int getPoints() {
    return nPoints;
  }

  /**
   * Add to the player's score
   *
   * @param nPoints
   *          the amount of points to add to the player's score
   */
  public void addPoints(int nPoints) {
    this.nPoints += nPoints;
  }

  /**
   * Get the WebSocket
   *
   * @return the socket
   */
  public QinqWebSocketAddapter getSocket() {
    return socket;
  }

  /**
   * Set the WebSocket
   *
   * @param socket
   *          the socket to set
   */
  public void setSocket(QinqWebSocketAddapter socket) {
    this.socket = socket;
  }
}
