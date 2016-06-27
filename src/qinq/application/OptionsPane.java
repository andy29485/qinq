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

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import qinq.resource.Game;
import qinq.resource.Question;

public class OptionsPane extends BorderPane {
  TextArea questions;

  public OptionsPane(GameUI root, Game game) {
    Label header = new Label("Game Options");
    header.setId("header");
    // header.resize(this.getWidth(), 100);// TODO should be in css

    HBox bottom = new HBox(20);
    Button buttonStart = new Button("Start");
    Button buttonSet = new Button("Back");
    Button buttonExit = new Button("Exit");

    buttonStart.setOnAction(e -> {
      root.startGame();
    });

    buttonSet.setOnAction(e -> {
      root.goToSetup();
    });

    buttonExit.setOnAction(e -> {
      root.exit();
    });
    bottom.getChildren().add(buttonStart);
    bottom.getChildren().add(buttonSet);
    bottom.getChildren().add(buttonExit);
    bottom.setAlignment(Pos.CENTER);

    // TODO create an option for this
    Question.setNumAnswers(2);

    questions = new TextArea();

    this.setTop(header);
    this.setCenter(questions);
    this.setBottom(bottom);
  }

  public List<String> getQuestions() {
    List<String> lstrQuestions = new ArrayList<String>();
    for (String question : this.questions.getText().split("\\s*\\n\\s*"))
      lstrQuestions.add(question);
    return lstrQuestions;
  }
}
