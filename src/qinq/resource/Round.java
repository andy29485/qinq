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
import java.util.concurrent.ThreadLocalRandom;

/**
 * Round.
 *
 * Defines one round of the game
 *
 * @author az
 * @version 1.0, 2016-06-20
 *
 */
public class Round extends GameObject {
  /**
   * Name of the round to display
   */
  private int            strRoundName;

  /**
   * Questions associated with this round
   */
  private List<Question> questions;

  /**
   * @param nRoundType
   *          Defines the type of round that this will be.
   *
   *          <p>
   *          Types:
   *          <ol start=0>
   *          <li>Normal Round - N questions per person, with N people per
   *          question, all unique</li>
   *          <li>Final Round - 1 question per person, all player have the same
   *          question</li>
   *          </ol>
   * @param strRoundName
   *          Name of the round, used when displaying to the players.
   * @param players
   *          players that will participate in this round.
   * @param questions
   *          list of strings that can potentially be used as questions.
   */
  public Round(int nRoundType, String strRoundName, List<Player> players,
      List<String> questions) {
    List<Player> tmpPlayers = new ArrayList<Player>();
    int random;

    for (int i = 0; i < Question.getNumAnswers(); i++) {
      tmpPlayers.addAll(players);
    }

    switch (nRoundType) {
      case 0:// Normal
        for (int i = 0; i < players.size(); i++) {
          List<Player> playersToAdd = new ArrayList<Player>();
          for (int j = 0; j < Question.getNumAnswers(); j++) {
            do {
              random = ThreadLocalRandom.current().nextInt(tmpPlayers.size());
            } while (!playersToAdd.contains(tmpPlayers.get(random)));
            playersToAdd.add(tmpPlayers.remove(random));
          }
          random = ThreadLocalRandom.current().nextInt(questions.size());
          String strQ = questions.remove(random);
          Question q = new Question(strQ, (Player[]) playersToAdd.toArray());
          this.questions.add(q);
        }
        break;
      case 1:// Final
        // TODO
        break;
    }
  }

  /**
   * Get the round's name
   *
   * @return the Round's Name
   */
  public int getRoundName() {
    return strRoundName;
  }

  /**
   * Set the round's name
   *
   * @param strRoundName
   *          the name to set
   */
  public void setRoundName(int strRoundName) {
    this.strRoundName = strRoundName;
  }
}
