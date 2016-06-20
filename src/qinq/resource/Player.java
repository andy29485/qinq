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
  private String            strName;
  /**
   * List of Answers for the current Game/Round
   *
   * TODO decide whether game or round
   */
  private List<Answer>      answers;
  /**
   * Socket used to connect to the player
   */
  private InetSocketAddress socket;

  public Player(String strName, InetSocketAddress socket) {
    this.answers = new ArrayList<Answer>();
    this.strName = strName;
    this.socket = socket;
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
   * @return the answers
   */
  public List<Answer> getAnswers() {
    return this.answers;
  }

}
