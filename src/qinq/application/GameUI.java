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

package qinq.application;

import javafx.scene.control.ScrollPane;
import qinq.resource.Game;

public class GameUI extends ScrollPane {
  private GameServer  server;
  private Game        game;
  private SetupPane   setup;
  private OptionsPane options;

  public GameUI(GameServer server, Game game) {
    this.game = game;
    this.server = server;
    this.setup = new SetupPane(this, server, game);
    this.options = new OptionsPane(this, game);

    this.game.setGameUI(this);

    this.goToSetup();
  }

  public void goToSetup() {
    this.setContent(this.setup);
  }

  public void goToOptions() {
    this.setContent(this.options);
  }

  public void refreshPlayers() {
    this.setup.refreshPlayers();
  }

  public void startGame() {
    // TODO game.start(questions);
    // TODO setContent to some pane to display during the game
    // TODO return to setup when game finishes? OR should this be done by above?
  }
}
