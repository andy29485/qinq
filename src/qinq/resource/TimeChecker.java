/**
 *
 */
package qinq.resource;

/**
 * TimeChecker
 *
 * Checks if the timer can continue
 *
 * @author az
 *
 */

@FunctionalInterface
public interface TimeChecker {
  /**
   * Checks if the timer should move on with the game
   * 
   * @return true if the timer should end
   */
  public boolean canMoveOn();
}
