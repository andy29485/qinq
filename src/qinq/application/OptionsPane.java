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

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import qinq.resource.Game;
import qinq.resource.Question;

public class OptionsPane extends BorderPane {
  public OptionsPane(GameUI root, Game game) {
    Label header = new Label("Game Options");
    header.setId("header");
    header.resize(this.getWidth(), 100);// TODO should be in css

    // TODO create an option for this
    Question.setNumAnswers(2);

    this.setTop(header);
    this.setCenter(new Label("TODO"));
  }
}
