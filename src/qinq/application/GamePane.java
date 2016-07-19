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

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import qinq.resource.Game;
import qinq.resource.Player;

public class GamePane extends BorderPane {
  private Game     game;
  private Label    labelTime;
  private Label    labelState;
  private HBox     header;
  private FlowPane players;

  public GamePane(Game game) {
    this.game = game;
    this.labelState = new Label("Answering");
    this.labelTime = new Label("");
    this.header = new HBox();
    this.players = new FlowPane();

    this.header.getStyleClass().add("header");
    this.players.getStyleClass().add("players");

    this.header.getChildren().addAll(this.labelState, this.labelTime);
    this.setContent(players);

    this.setTop(header);
  }

  public void refresh() {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        if (GamePane.this.game.getRound() != null
            && GamePane.this.game.getRound().getTime() > 0)
          GamePane.this.labelTime.setText(
              String.format(" - %d", GamePane.this.game.getRound().getTime()));
        else
          GamePane.this.labelTime.setText("");
        if (GamePane.this.labelState.getText().equalsIgnoreCase("Answering")) {
          GamePane.this.players.getChildren().clear();
          GamePane.this.players.getChildren().add(new Label("Waiting on:"));
          for (Player p : GamePane.this.game.getPlayers()) {
            if (p.getAnswers().size() > 0)
              GamePane.this.players.getChildren().add(p.getLargeLabel());
          }
        }
      }
    });
  }

  public void changeState(String state) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        GamePane.this.labelState.setText(state);
        if (state.equalsIgnoreCase("Answering"))
          GamePane.this.setContent(GamePane.this.players);
      }
    });
  }

  public void setContent(Node node) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        GamePane.this.setCenter(node);
      }
    });
  }

}
