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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import qinq.resource.Player;

public class SetupPane extends BorderPane {
  private Label    addressLabel;
  private FlowPane players;

  public SetupPane(GameUI root, GameServer server) {
    this.addressLabel = new Label(server.getAddress());
    this.players = new FlowPane();

    this.setId("setup-pane");
    this.players.getStyleClass().add("players");

    HBox top = new HBox(5);
    HBox bottom = new HBox(20);

    top.getStyleClass().add("top");
    bottom.getStyleClass().add("bottom");

    Button buttonStart = new Button("Start");
    Button buttonOpt = new Button("Options");
    Button buttonExit = new Button("Exit");

    this.addressLabel.setId("address-label");
    top.getStyleClass().add("header");
    top.getChildren().add(new Label("Go to: "));
    top.getChildren().add(this.addressLabel);
    top.getChildren().add(new Label(" to start playing"));

    buttonStart.setOnAction(e -> {
      root.startGame();
    });

    buttonOpt.setOnAction(e -> {
      root.goToOptions();
    });

    buttonExit.setOnAction(e -> {
      root.exit();
    });
    bottom.getChildren().add(buttonStart);
    bottom.getChildren().add(buttonOpt);
    bottom.getChildren().add(buttonExit);

    this.setTop(top);
    this.setCenter(this.players);
    this.setBottom(bottom);
  }

  public void addPlayer(Player p) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        SetupPane.this.players.getChildren().add(p.getLargeLabel());
      }
    });
  }
}
