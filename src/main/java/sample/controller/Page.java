package sample.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.media.Media;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.FirstPage;
import sample.models.Data;

import java.io.File;
import java.nio.file.Paths;

import static sample.view.print;

public class Page extends Application {

    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        new FirstPage().run();
        stage.show();
    }


    public static Popup createPopup(final String message) {

        final Popup popup = new Popup();
        popup.setX(stage.getX() + 15);
        popup.setY(stage.getY() + stage.getHeight() - 65);
        popup.setHideOnEscape(true);

        Label label = new Label(" " + message + " ");
        label.setAlignment(Pos.CENTER);

        label.setMinWidth(80);
        label.setMinHeight(50);
        label.getStyleClass().add("popup1");

        popup.getContent().add(label);

        return popup;
    }

    public static void showPopupMessage(final String message) {

        final Popup popup = createPopup(message);
        popup.show(stage);

        Timeline idlestage = new Timeline(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popup.hide();
            }
        }));
        idlestage.setCycleCount(1);
        idlestage.play();
    }


    public static void setBackButton(Button button) {

        button.setMinSize(40,40);
        button.setMaxSize(40,40);

        button.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.getScene().setCursor(Cursor.HAND);
            }
        });

        button.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.getScene().setCursor(Cursor.DEFAULT);
            }
        });

        Image img = new Image("pic/backArrow.png");
        ImageView view = new ImageView(img);

        view.fitHeightProperty().bind(button.heightProperty());
        view.fitWidthProperty().bind(button.widthProperty());
        button.setGraphic(view);
    }

    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run()
            {
                if(game.isGameInProgress){
                    Data.saveData();
                }
            }
        });

        DataBaseController.createUsersDirectory();
        DataBaseController.createMapsDirectory();
        launch(args);


    }



}


