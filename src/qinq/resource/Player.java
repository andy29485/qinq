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
public class Player extends GameObject {
  /**
   * The nick name the player will go by during the game
   */
  private String       strName;
  /**
   * The ip used to connect, primarily used to reconnect
   */
  private String       strIp;
  /**
   * List of Answers for the current Game/Round
   *
   * TODO decide whether game or round
   */
  private List<Answer> answers;
  /**
   * Number of votes a player has.
   */
  private int          nVotes;
  /**
   * Total number of players, use for generating player IDs.
   */
  private static int   nPlayers = 0;

  public Player(String strName) {
    super(Player.nPlayers++);
    this.answers = new ArrayList<Answer>();
    this.strName = strName;
    this.setVotes(0);
  }

  /**
   * Get player name
   *
   * @return the strName
   */
  public String getName() {
    return this.strName;
  }

  /**
   * Set player name
   *
   * @param strName
   *          the strName to set
   */
  public void setName(String strName) {
    this.strName = strName;
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
   * Get the player IP address
   * 
   * @return the IP to set
   */
  public String getIp() {
    return this.strIp;
  }

  /**
   * Set the player IP address
   * 
   * @param strIp
   *          the IP to set
   */
  public void setIp(String strIp) {
    this.strIp = strIp;
  }

}
