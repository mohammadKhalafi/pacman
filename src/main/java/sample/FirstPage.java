package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sample.controller.Audio;
import sample.controller.Page;
import sample.controller.game;

import java.io.IOException;

public class FirstPage extends Page {

    public void run() {

        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);

        BorderPane mainPain = new BorderPane();
        mainPain.setTop(Audio.getAudioButton());
        pane.setAlignment(Pos.CENTER);
        mainPain.setCenter(pane);
        mainPain.setId("pane");

        Scene scene = new Scene(mainPain, 800, 600);
        scene.getStylesheets().add("./Css/css.CSS");

        pane.add(getRegisterButton(scene), 0, 0);
        pane.add(getLoginButton(scene), 0, 1);
        pane.add(getScoreBoardButton(scene), 0, 2);
        pane.add(getGameButton(scene), 0, 3);

        stage.setTitle("pack man");
        stage.setScene(scene);
    }


    public Button getGameButton(Scene scene) {

        Button button = new Button("start game");
        button.setText("game");
        button.setId("myButton");
        setCursorForButton(button, scene);

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                game.startGame(null, gamePage.life, 2);
            }
        });
        return button;

    }

    public Button getScoreBoardButton(Scene scene) {

        Button button = new Button("score board");
        button.setText("score board");
        button.setId("myButton");
        setCursorForButton(button, scene);

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                new ScoreBoard().run(null);
            }
        });

        return button;
    }

    public Button getLoginButton(Scene scene) {

        Button button = new Button("login");
        button.setText("Login");
        button.setId("myButton");
        setCursorForButton(button, scene);

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                new LoginMenu().run();
            }
        });

        return button;
    }

    public Button getRegisterButton(Scene scene) {

        Button button = new Button("register");
        button.setText("Register");
        button.setId("myButton");
        setCursorForButton(button, scene);

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                Node node = (Node) mouseEvent.getSource();
                Stage thisStage = (Stage) node.getScene().getWindow();

                try {
                    Parent root = FXMLLoader.load(getClass().getResource("fxml/RegisterMenu.fxml"));
                    root.setId("pane");
                    thisStage.getScene().setRoot(root);

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });


        return button;
    }

    public void setCursorForButton(Button button, Scene scene) {

        button.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                scene.setCursor(Cursor.HAND);
                button.setId("myButton2");
            }
        });

        button.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                scene.setCursor(Cursor.DEFAULT);
                button.setId("myButton");
            }
        });
    }
}
