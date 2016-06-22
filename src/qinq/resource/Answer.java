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

import java.util.HashMap;
import java.util.Map;

/**
 * Answer
 *
 * @author az
 * @version 1.0, 2016-06-20
 */
public class Answer extends GameObject {
  /**
   * The Player answering the question
   */
  private Player               p;
  /**
   * The questions linked to this answer
   */
  private Question             q;
  /**
   * The actual value for this answer
   */
  private String               strAnswer;
  /**
   * List of people that vote for this question
   */
  private Map<Player, Integer> votes;
  /**
   * Total number of answers, use for generating answer IDs.
   */
  private static int           nAnswers = 0;

  /**
   * @param p
   *          player answering the question
   * @param q
   *          question linked to this answer
   */
  public Answer(Player p, Question q) {
    super(Answer.nAnswers++);
    this.p = p;
    this.q = q;
    this.votes = new HashMap<Player, Integer>();
    this.p.getAnswers().add(this);
  }

  /**
   * Get the player that is answering the question
   *
   * @return the {@link Player} answering this question
   */
  public Player getPlayer() {
    return this.p;
  }

  /**
   * Get the player's answer
   *
   * @return the answer the player entered
   */
  public String getAnswer() {
    return this.strAnswer;
  }

  /**
   * Set the answer of the player
   *
   * @param strAnswer
   *          the answer to set
   */
  public void setAnswer(String strAnswer) {
    this.strAnswer = strAnswer;
  }

  /**
   * Get the question text
   *
   * @return the question string
   */
  public String getQuestion() {
    return this.q.getQuestion();
  }

  /**
   * Check if the questions has been answered
   *
   * @return whether or not the player has answered the question
   */
  public boolean isAnswered() {
    return !this.strAnswer.isEmpty();
  }

  /**
   * Get the players that voted for this question
   *
   * @return the votes
   */
  public Map<Player, Integer> getVotes() {
    return votes;
  }

  /**
   * Get the players that voted for this question
   *
   * @return the votes
   */
  public int vote(Player p) {
    if (p.getVotes() > 1) {
      if (this.votes.get(p) == null)
        this.votes.put(p, 1);
      else
        this.votes.replace(p, this.votes.get(p) + 1);
      p.useVote();
    }
    return this.votes.get(p);
  }

}
