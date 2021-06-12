package sample;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import sample.controller.Board;
import sample.controller.DataBaseController;
import sample.controller.Page;
import sample.controller.game;
import sample.models.User;

import java.util.Collections;

public class gamePage extends Page {

    BorderPane pane = new BorderPane();
    HBox lifeBox = new HBox();
    public static int life = 3;
    Label lifeLabel = new Label(life + "");
    Button increaseLife = new Button();
    Button decreaseLife = new Button();
    VBox mapButtons = new VBox();
    ScrollPane scrollPane = new ScrollPane();
    Button createNewMapButton = new Button();
    VBox allButtons = new VBox();

    User user;

    private void addMap(int mapId) {

        Button button = new Button();

        button.setMinSize(100, 10);
        button.setStyle("-fx-background-color: #6363d7");
        button.setAlignment(Pos.CENTER);
        button.setText("map " + mapId);

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                game.startGame(user, life, mapId);
            }
        });
        mapButtons.getChildren().add(button);
    }

    public void run(User user) {

        this.user = user;
        BackgroundImage bI0 = new BackgroundImage(new Image
                ("/pic/background1.jpg"), BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(decreaseLife.getWidth(), decreaseLife.getHeight(),
                        true, true, true, false));
        pane.setBackground(new Background(bI0));

        mapButtons.setSpacing(10);
        mapButtons.setAlignment(Pos.CENTER);

        Collections.sort(user.getMaps());

        for (int mapId : user.getMaps()) {

            addMap(mapId);
        }

        scrollPane.setMaxWidth(200);
        scrollPane.setContent(mapButtons);
        scrollPane.setFitToWidth(true);
        scrollPane.setMaxHeight(300);
        scrollPane.setMinHeight(300);
        scrollPane.setPannable(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);


        createNewMapButton.setAlignment(Pos.CENTER);
        createNewMapButton.setText("add new map");
        createNewMapButton.setMinSize(30, 30);

        createNewMapButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int id = Board.getRandomBoard();
                user.addMap(id);
                DataBaseController.upDateUser(user);
                addMap(id);
            }
        });

        allButtons.setSpacing(10);
        allButtons.getChildren().add(scrollPane);
        allButtons.getChildren().add(createNewMapButton);
        allButtons.setAlignment(Pos.CENTER);
        pane.setCenter(allButtons);

        increaseLife.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (life >= 5) {
                    showPopupMessage("max is 5");
                    return;
                }
                life++;
                lifeLabel.setText(life + "");
            }
        });

        Image image1 = new Image("/pic/menha.png");
        decreaseLife.setMaxSize(20, 20);
        decreaseLife.setMinSize(20, 20);

        BackgroundImage bI1 = new BackgroundImage(image1, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(decreaseLife.getWidth(), decreaseLife.getHeight(),
                        true, true, true, false));

        decreaseLife.setBackground(new Background(bI1));

        decreaseLife.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (life < 3) {
                    showPopupMessage("min is 2");
                    return;
                }
                life--;
                lifeLabel.setText(life + "");
            }
        });

        increaseLife.setMaxSize(20, 20);
        increaseLife.setMinSize(20, 20);

        Image image2 = new Image("/pic/beezafe.png");

        BackgroundImage bI = new BackgroundImage(image2, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(increaseLife.getWidth(), increaseLife.getHeight(),
                        true, true, true, false));


        increaseLife.setBackground(new Background(bI));

        lifeBox.setSpacing(5);
        lifeBox.setAlignment(Pos.CENTER);
        lifeBox.getChildren().add(decreaseLife);
        lifeBox.getChildren().add(lifeLabel);
        lifeBox.getChildren().add(increaseLife);
        pane.setBottom(lifeBox);
        stage.getScene().setRoot(pane);
    }
}
