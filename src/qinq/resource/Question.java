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

import java.util.List;

/**
 * Question
 *
 * @author az
 * @version 1.0, 2016-06-20
 */
public class Question extends GameObject {
  /**
   * The actual question that is asked
   */
  private String       strValue;
  /**
   * List of answers that are assocciated with this question
   */
  private List<Answer> lAnswers;
  /**
   * Number of answers each question should have
   */
  private static int   nAnswers;

  public Question(String strValue, Player... players) {
    this.strValue = strValue;
    for (Player player : players) {
      this.lAnswers.add(new Answer(player, this));
    }
  }

  /**
   * Get the question
   *
   * @return the question
   */
  public String getQuestion() {
    return this.strValue;
  }

  /**
   * Set the question
   *
   * @param strValue
   *          the text value for this question
   */
  public void setQuestion(String strValue) {
    this.strValue = strValue;
  }

  /**
   * Get the number of answers each question does/will have
   *
   * @return the number of answers a question should have
   */
  public static int getNumAnswers() {
    return Question.nAnswers;
  }

  /**
   * Set the number of answers all questions should have
   *
   * @param nAnswers
   *          the number of answers a questions should have
   */
  public static void setNumAnswers(int nAnswers) {
    Question.nAnswers = nAnswers;
  }

}
