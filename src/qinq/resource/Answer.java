/**
 *
 */
package qinq.resource;

/**
 * Answer
 *
 * @author az
 * @version 1.0, 2016-06-20
 */
public class Answer extends GameObject {
  private Player   p;
  private Question q;
  private String   strAnswer;

  public Answer(Player p, Question q, int nId) {
    super(nId);
    this.p = p;
    this.q = q;
    // TODO accossiate answer with player
  }

  /**
   * @return the {@link Player} answering this question
   */
  public Player getPlayer() {
    return this.p;
  }

  /**
   * @return the answer the player entered
   */
  public String getAnswer() {
    return this.strAnswer;
  }

  /**
   * @param strAnswer
   *          the answer to set
   */
  public void setAnswer(String strAnswer) {
    this.strAnswer = strAnswer;
  }

  public String getQuestion() {
    return this.q.getQuestion();
  }

  /**
   * @return whether or not the player has answered the question
   */
  public boolean isAnswered() {
    return !this.strAnswer.isEmpty();
  }

}
