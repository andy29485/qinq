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

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

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
  /**
   * Number of answers each question should have
   */
  private static int   dAnswerTime = 40;
  /**
   * Number of answers each question should have
   */
  private static int   dVoteTime   = 15;
  /**
   * Total number of questions, use for generating question IDs.
   */
  private static int   nQuestions  = 0;

  public Question(String strValue, List<Player> players) {
    super(Question.nQuestions++);

    this.strValue = strValue;
    this.lAnswers = new ArrayList<Answer>();

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

  /**
   * Get the answers assocciated with this question
   *
   * @return the answers assocciated with this question
   */
  public List<Answer> getAnswers() {
    return this.lAnswers;
  }

  /**
   * Get the amount of time one has to answer a single question
   *
   * @return the amount of time one has to answer a single question
   */
  public static int getAnswerTime() {
    return Question.dAnswerTime;
  }

  /**
   * Set the amount of time one has to answer a single question
   *
   * @param dAnswerTime
   *          the amount of time one has to answer a single question
   */
  public static void setAnswerTime(int dAnswerTime) {
    Question.dAnswerTime = dAnswerTime;
  }

  /**
   * Get the amount of time one has to cast a single vote
   *
   * @return the amount of time one has to cast a single vote
   */
  public static int getVoteTime() {
    return Question.dVoteTime;
  }

  /**
   * Set the amount of time one has to cast a single vote
   *
   * @param dVoteTime
   *          the amount of time one has to cast a single vote
   */
  public static void setVoteTime(int dVoteTime) {
    Question.dVoteTime = dVoteTime;
  }

  /**
   * Check if a player is answering this question
   *
   * @param p
   *          the player to check
   * @return true if the player is answering the question
   */
  public boolean isAnswering(Player p) {
    for (Answer a : this.lAnswers) {
      if (a.getPlayer().equals(p))
        return true;
    }
    return false;
  }

  /**
   * Get the voting choices for this question in a displayable form
   *
   * @return a pane with the question and answers for this question
   */
  public Node getVotingPane() {
    BorderPane voting = new BorderPane();
    voting.setTop(new Label(this.strValue));

    FlowPane answers = new FlowPane();
    for (Answer answer : this.lAnswers)
      answers.getChildren().add(answer.getAnonAnswer());
    voting.setCenter(answers);

    return voting;
  }

  /**
   * Get the results for this questions in a displayable form
   *
   * @return a pane with the results for this question
   */
  public Node getResultsPane() {
    // TODO animation?
    BorderPane results = new BorderPane();
    results.setTop(new Label(this.strValue));

    FlowPane answers = new FlowPane();
    for (Answer answer : this.lAnswers)
      answers.getChildren().add(answer.getFinalAnswer());
    results.setCenter(answers);

    return results;
  }
}
