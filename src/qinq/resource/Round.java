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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import qinq.application.GamePane;

/**
 * Round.
 *
 * Defines one round of the game
 *
 * @author az
 * @version 1.0, 2016-06-20
 *
 */
public class Round {
  /**
   * Name of the round to display
   */
  private String         strRoundName;
  /**
   * Questions associated with this round
   */
  private List<Question> questions;
  /**
   * Question currently being voted on
   */
  private Question       question;
  /**
   * Time left(in seconds) for the current action
   */
  private int            dTime;
  /**
   * The GamePane on which stuff will be displayed on for the GM
   */
  private GamePane       display;
  /**
   * List of player 'participating' in this round
   */
  private List<Player>   players;
  /**
   * LiThe type of round that this is
   */
  private int            nRoundType;

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
   * @param display
   *          the GamePane on which stuff will be displayed on for the GM
   */
  public Round(int nRoundType, String strRoundName, List<Player> players,
      List<String> questions, GamePane display) {
    List<Player> tmpPlayers; // for question distribution
    this.display = display; // the display on the main window
    int random;
    this.dTime = 0;
    this.questions = new ArrayList<Question>(); // list of questions to be used
    this.players = players; // list of players
    this.nRoundType = nRoundType;

    switch (nRoundType) {
      case 0:// Normal
        List<Player[]> question_groups = new ArrayList<Player[]>();
        grouping:
        do {
          tmpPlayers = new ArrayList<Player>();
          for (int i = 0; i < Question.getNumAnswers(); i++) { // add players
            tmpPlayers.addAll(players);// into a 'hat' that will be drawn from
                                       // when grouping people for answers
          }
          for (int i = 0; i < players.size(); i++) {
            Player[] group = new Player[Question.getNumAnswers()];
            for (int j = 0; j < Question.getNumAnswers(); j++) {
              do {
                if (tmpPlayers.size() < Question.getNumAnswers()
                    && tmpPlayers.size() > 1
                    && (new HashSet<Player>(tmpPlayers)).size() == 1)
                  continue grouping;
                random = ThreadLocalRandom.current().nextInt(tmpPlayers.size());
              } while (Arrays.asList(group).contains(tmpPlayers.get(random)));
              group[j] = tmpPlayers.remove(random);
            }
            question_groups.add(group);
          }
        } while (tmpPlayers.size() > 0);
        for (Player[] group : question_groups) {
          random = ThreadLocalRandom.current().nextInt(questions.size());
          String strQ = questions.remove(random);
          Question q = new Question(strQ, group);
          this.questions.add(q);
        }
        break;
      case 1:// Final
        random = ThreadLocalRandom.current().nextInt(questions.size());
        String strQ = questions.remove(random);
        Question q = new Question(strQ, (Player[]) this.players.toArray());
        this.questions.add(q);
        break;
    }
  }

  /**
   * Get the round's name
   *
   * @return the Round's Name
   */
  public String getRoundName() {
    return this.strRoundName;
  }

  /**
   * Get the questions used in this round
   *
   * @return the Round's Name
   */
  public List<Question> getQuestions() {
    return this.questions;
  }

  /**
   * Get the answers used in this round
   *
   * @return the Round's Name
   */
  public List<Answer> getAnswers() {
    List<Answer> answers = new ArrayList<Answer>();
    for (Question question : this.questions)
      answers.addAll(question.getAnswers());
    return answers;
  }

  /**
   * Set the round's name.
   *
   * @param strRoundName
   *          the name to set.
   */
  public void setRoundName(String strRoundName) {
    this.strRoundName = strRoundName;
  }

  /**
   * Get the currently selected question.
   *
   * @return the currently selected question.
   */
  public Question getQuestion() {
    return question;
  }

  /**
   * Get the amount of time left for the current action.
   *
   * There is an extra three seconds added just in case of lag.
   *
   * @return the number of seconds left until the current action is over.
   */
  public int getTime() {
    return this.dTime;
  }

  /**
   * Wait for a given number of seconds
   *
   * @param time
   *          number of seconds to wait
   * @param tc
   *          functional interface that returns true when the timer should stop
   *          counting down
   */
  public void wait(int time, TimeChecker tc) {
    this.dTime = time;
    while (this.dTime > 0 && (tc == null || !tc.canMoveOn())) {
      try {
        Thread.sleep(1000);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
      this.dTime--;
      this.display.refresh();
    }
    try {
      Thread.sleep(3000);
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Calculate the amount of time needed to answer the questions and wait for
   * that long.
   */
  public void answer() {
    this.display.changeState("Answering");
    this.wait(Question.getAnswerTime() * Question.getNumAnswers(), () -> {
      for (Player p : this.players)
        if (p.getAnswers().size() > 0)
          return false;
      return true;
    });
  }

  /**
   * Calculate the amount of time needed to vote the questions and wait for that
   * long.
   *
   * Also display the current questions that is being voted on.
   */
  public void vote() {
    for (Question question : this.questions) {
      this.display.changeState("Voting");
      this.setVotes();

      this.question = question;
      this.display.setConent(this.question.getVotingPane());
      this.wait(Question.getVoteTime() * this.question.getAnswers().size(),
          () -> {
            for (Player p : this.players)
              if (p.getVotes() > 0)
                return false;
            return true;
          });

      this.displayResults();
    }
  }

  /**
   * Display the result for the current question and move on after three seconds
   */
  public void displayResults() {
    this.display.changeState("Results");
    this.display.setConent(this.question.getResultsPane());
    this.wait(12, null);
  }

  /**
   * Save the result of the current question to a file.
   */
  public void saveResults() {
    // TODO save question results
  }

  /**
   * Set the number of votes each player has
   */
  public void setVotes() {
    for (Player p : this.players) {
      switch (this.nRoundType) {
        case (0):
          p.setVotes(1);
          break;
        case (1):
          p.setVotes(3);
          break;
      }
    }
  }
}
