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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import qinq.resource.Game;
import qinq.resource.Player;

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
    this.setFitToWidth(true);
    this.setFitToHeight(true);
  }

  public void goToSetup() {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        GameUI.this.setContent(GameUI.this.setup);
      }
    });
  }

  public void goToOptions() {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        GameUI.this.setContent(GameUI.this.options);
      }
    });
  }

  public void addPlayer(Player p) {
    this.setup.addPlayer(p);
  }

  public void exit() {
    this.server.stop();
    System.exit(0);
  }

  public void setGame(Game g) {
    this.game = g;
  }

  public void startGame() {

    // This stuff probably should not be here, or maybe it should?
    List<String> questions = new ArrayList<String>();
    for (String question : this.options.getQuestions())
      questions.add(question);

    InputStream in = this.getClass()
        .getResourceAsStream("/qinq/resource/questions/misc.txt");
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String question;
    try {
      while ((question = br.readLine()) != null)
        questions.add(question);
    }
    catch (IOException e) {
      e.printStackTrace();
    }

    GamePane display = new GamePane(game);
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        GameUI.this.setContent(display);
      }
    });
    switch (this.game.start(questions, display, this)) {
      case 1:
        new Alert(Alert.AlertType.ERROR,
            "Game Has started, how are you doing this?").showAndWait();
        this.goToSetup();
        break;
      case 2:
        new Alert(Alert.AlertType.ERROR, "Not enough players").showAndWait();
        this.goToSetup();
        break;
      case 3:
        new Alert(Alert.AlertType.ERROR, "Not enough Questions").showAndWait();
        this.goToSetup();
        break;
    }
  }
}
