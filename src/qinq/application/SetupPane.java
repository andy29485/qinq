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

import org.json.JSONObject;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import qinq.resource.Game;
import qinq.resource.Player;

public class SetupPane extends BorderPane {
  private VBox       addressLabels;
  private FlowPane   players;
  private Game       game;
  private GameServer server;

  public SetupPane(GameUI root, GameServer server, Game game) {
    this.addressLabels = new VBox();
    this.players = new FlowPane();
    this.game = game;
    this.server = server;

    this.resetAddresses();

    this.setId("setup-pane");
    this.players.getStyleClass().add("players");

    HBox top = new HBox(5);
    HBox bottom = new HBox(20);

    top.getStyleClass().add("top");
    bottom.getStyleClass().add("bottom");

    Button buttonStart = new Button("Start");
    Button buttonOpt = new Button("Options");
    Button buttonExit = new Button("Exit");

    this.addressLabels.setId("address-labels");
    top.getStyleClass().add("header");
    top.getChildren().add(new Label("Go to: "));
    top.getChildren().add(this.addressLabels);
    top.getChildren().add(new Label(" to start playing"));

    HBox.setHgrow(this.addressLabels, Priority.ALWAYS);

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

  public void resetAddresses() {
    this.addressLabels.getChildren().clear();
    this.addAddress(this.server.getAddress());
  }

  public void addAddress(String address) {
    TextField copyable = new TextField(address);
    copyable.setEditable(false);
    copyable.getStyleClass().add("copyable-label");
    this.addressLabels.getChildren().add(copyable);
  }

  public void addPlayer(Player p) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        Node player = p.getLargeLabel();
        player.setOnMouseClicked(event -> {
          Alert alert = new Alert(AlertType.CONFIRMATION);
          alert.setTitle("Confirmation Dialog");
          alert.setHeaderText("Player will be Kicked");
          alert.setContentText("Are you sure you wish to kick this player?");
          if (alert.showAndWait().get() == ButtonType.OK) { // ... user chose OK
            p.getSocket().sendText(new JSONObject().put("action", "kick"));
            SetupPane.this.players.getChildren().remove(player);
            SetupPane.this.game.getPlayers().remove(p);
          }
        });

        SetupPane.this.players.getChildren().add(player);
      }
    });
  }
}
