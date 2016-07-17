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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import qinq.resource.Game;
import qinq.resource.Question;

public class OptionsPane extends BorderPane {
  TextArea                   questions;
  Map<CheckBox, Set<String>> categories;
  FlowPane                   categoryPane;
  VBox                       options;

  public OptionsPane(GameUI root, Game game) {
    Label header = new Label("Game Options");
    header.getStyleClass().add("header");

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

    Question.setNumAnswers(2);
    Spinner<Integer> num_answers = new Spinner<Integer>(2, 6, 2);
    num_answers.valueProperty().addListener(
        (obs, oldValue, newValue) -> Question.setNumAnswers(newValue));

    Game.setMaxPlayers(10);
    Spinner<Integer> max_players = new Spinner<Integer>(5, 10, 10);
    max_players.valueProperty()
        .addListener((obs, oldValue, newValue) -> Game.setMaxPlayers(newValue));
    Game.setMinPlayers(3);
    Spinner<Integer> min_players = new Spinner<Integer>(3, 9, 3);
    min_players.valueProperty()
        .addListener((obs, oldValue, newValue) -> Game.setMinPlayers(newValue));

    this.categories = new HashMap<CheckBox, Set<String>>();
    this.questions = new TextArea();
    this.categoryPane = new FlowPane();
    this.options = new VBox();
    this.options.setId("options");
    this.refresh();

    this.questions.setTooltip(new Tooltip("One Question per Line"));
    this.categoryPane.setId("categories:");

    this.options.getChildren().addAll(new Separator(Orientation.HORIZONTAL),
        new Label("Categories:"), this.categoryPane);

    this.options.getChildren().addAll(new Separator(Orientation.HORIZONTAL),
        new Label("Custom Questions:"), this.questions);

    this.options.getChildren().addAll(new Separator(Orientation.HORIZONTAL),
        new HBox(10, new Label("Number of Answers per question:"), num_answers),
        new HBox(20, new Label("Min Players:"), min_players),
        new HBox(20, new Label("Max Players:"), max_players));

    this.setTop(header);
    this.setCenter(this.options);
    this.setBottom(bottom);
    this.refresh();
  }

  public Set<String> getQuestions() {
    Set<String> lstrQuestions = new HashSet<String>(); // set of questions to
                                                       // use

    // add questions from custom question text area
    for (String question : this.questions.getText().split("\\s*\\n\\s*"))
      if (!question.isEmpty())
        lstrQuestions.add(question);

    // Add questions by category(if selected)
    for (CheckBox cb : this.categories.keySet())
      if (cb.isSelected())
        lstrQuestions.addAll(this.categories.get(cb));

    return lstrQuestions;
  }

  public void refresh() {
    // Create a temporary container for categories: map check-box -> questions
    Map<CheckBox, Set<String>> tmp_categories =
        new HashMap<CheckBox, Set<String>>();

    // Add categories from jar
    InputStream in = this.getClass()
        .getResourceAsStream("/qinq/resource/questions/categories.txt");
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String category;
    try {
      // for each category specified in the categories.txt file
      while ((category = br.readLine()) != null) {
        if (this.getClass().getResourceAsStream(
            "/qinq/resource/questions/" + category) != null) {// check if it
                                                              // exists
          populateCategories( // Then associate questions in that file with
              new BufferedReader( // a check box
                  new InputStreamReader(this.getClass().getResourceAsStream(
                      "/qinq/resource/questions/" + category))),
              tmp_categories, category);
        }
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }

    // Add custom categories
    File questions_dir = new File("questions");
    if (questions_dir.exists() && questions_dir.isDirectory()) {
      for (File category_file : questions_dir.listFiles((dir, name) -> {
        return name.toLowerCase().endsWith(".txt");
      })) {
        try {
          populateCategories(new BufferedReader(new FileReader(category_file)),
              tmp_categories, category_file.getName());
          ;
        }
        catch (FileNotFoundException e) {
          e.printStackTrace();
        }
      }
    }

    // Uncheck previously uncheced categories
    for (CheckBox cbNew : tmp_categories.keySet()) {
      cbNew.setSelected(true);
      for (CheckBox cbOld : this.categories.keySet()) {
        if (cbNew.getText().equalsIgnoreCase(cbOld.getText())) {
          cbNew.setSelected(cbOld.isSelected());
          break;
        }
      }
    }

    // Update
    this.categories = tmp_categories;
    this.categoryPane.getChildren().clear();
    for (CheckBox cb : this.categories.keySet()) {
      this.categoryPane.getChildren().add(cb);
    }
  }

  private static void populateCategories(BufferedReader br,
      Map<CheckBox, Set<String>> categories, String category) {
    // Create a collections for storing questions
    Set<String> questions = new HashSet<String>();

    if (category.matches("(?i)\\.txt$"))
      category = category.replaceAll("(?i)\\.txt$", "");

    try {
      String question; // String temporarily stores questions
      while ((question = br.readLine()) != null) { // read each line into
                                                   // the question variable
        if (!question.isEmpty())
          questions.add(question);// Add non-empty strings into question set
      }
      found:
      {
        for (CheckBox cb : categories.keySet()) {// if category exists append
                                                 // questions to existing
                                                 // category
          if (cb.getText().equalsIgnoreCase(category)) {
            categories.get(cb).addAll(questions);
            break found;
          }
        }
        // otherwise create a new category with said questions
        categories.put(new CheckBox(category), questions);
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
}
