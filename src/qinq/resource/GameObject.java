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

/**
 * Game Object
 *
 * TODO maybe this is not needed
 *
 * @author az
 * @version 1.0, 2016-06-20
 */
public class GameObject {
  /**
   * The id of the game object
   */
  private int nId;

  /**
   * Default constructor. Creates object with id 0.
   */
  public GameObject() {
    this(0);
  }

  /**
   * Constructor that creates object with specified id.
   */
  public GameObject(int nId) {
    this.nId = nId;
  }

  /**
   * Get the id of the game object
   *
   * @return the id of the game object
   */
  public int getID() {
    return this.nId;
  }

  /**
   * Set the id of a game object
   *
   * @param nId
   *          new id of object
   */
  public void setID(int nId) {
    this.nId = nId;
  }

}
