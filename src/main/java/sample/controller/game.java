package sample.controller;


import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import sample.FirstPage;
import sample.MainMenu;
import sample.gamePage;
import sample.models.*;

import static sample.controller.Board.*;
import static sample.view.print;

public class game extends Page {

    public static boolean isGameInProgress = false;
    public static game instance;
    public static User user = null;
    public boolean hasPaused = false;
    public boolean hasGameStarted = false;
    public int life = 0;
    public int score = 0;
    public Label scoreLabel = new Label();
    public HBox lifeBox = new HBox();
    public Pane pane;
    public Label message = new Label();
    public HBox buttons = new HBox();

    public static int sceneX = 800;
    public static int sceneY = 600;

    public static int gridPaneX;
    public static int gridPaneY;
    public boolean isContinueGame = false;
    private EventHandler<KeyEvent> filter;


    private game(int life) {

        this.life = life;
        hasGameStarted = false;
        initPane();
        setBoardPostion();
        if (user != null) {
            this.score = user.getScore();
        }
        setScoreLabel();
        setLifeBox();
        setButtons();
        setMessage();
        setListenerForKeyPressed();
    }


    private game(int boardID, int life) {

        this.life = life;
        hasGameStarted = false;

        Pacman.getInstance().board = Board.getBoardById(boardID);
        Pacman.getInstance().tr.passedTimeForSeialize = 10000L;
        initPane();
        setBoardPostion();
        if (user != null) {
            score = user.getScore();
        }
        setScoreLabel();
        setLifeBox();
        setMessage();
        setPacmanAndRohsPosition();
        setButtons();
        setListenerForKeyPressed();
    }

    public static game getCurrentInstance() throws RuntimeException {

        if (instance == null) {

            throw new RuntimeException("");
        }
        return instance;
    }

    public static void start2() {
        if (Pacman.getInstance().tr.passedTimeForSeialize != 10000L) {

            Pacman.getInstance().tr.pacmanTimeStart =
                    System.currentTimeMillis() - Pacman.getInstance()
                            .tr.passedTimeForSeialize;
        }
        roh.setStartTimes();
    }


    private static void play() {

        Pacman.getInstance().tr.play();
        Pacman.getInstance().tr2.play();
        for (roh roh : roh.rohs) {
            roh.tr.play();
        }
    }

    public static void stopAnimations() {

        for (roh roh1 : roh.rohs) {
            roh1.tr.stop();
        }
        Pacman.getInstance().tr.stop();
        Pacman.getInstance().tr2.stop();
    }

    public static void startGameByData() {

        Data data = Data.getLastData();
        user = DataBaseController.getUserByUsername(data.username);
        Pacman.initPacmanByData(data);
        roh.initRohsByData(data);
        instance = new game(data.life);

        if (data.hasGameStarted) {
            instance.isContinueGame = false;
            instance.hasGameStarted = true;
            stage.getScene().setRoot(instance.pane);
            start2();
            play();
        } else {
            instance.setMessageText();
            instance.isContinueGame = data.isContinueGame;
            instance.hasGameStarted = false;
            stage.getScene().setRoot(instance.pane);
            play();
        }
        isGameInProgress = true;
    }


    public void start() {

        game.instance.hasGameStarted = true;
        game.instance.removeMessage();

        roh.setStartTimes();

        if (game.instance.isContinueGame) {
            roh.undoPacmanTime();
        }

        for (roh roh : roh.rohs) {
            if (game.instance.isContinueGame) {
                roh.stopStart = 0L;
                roh.stopWait = 0L;
            } else {
                roh.stopWait = 2000L;
            }
            roh.interSectInvalidTimeWait = 2000L;
        }
    }


    public void setListenerForKeyPressed() {

        if (filter != null) {
            Page.stage.getScene().removeEventFilter(KeyEvent.ANY, filter);
        }

        filter = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

                if (!game.instance.hasGameStarted) {
                    if (keyEvent.getCode().isArrowKey()) {
                        start();
                    } else {
                        return;
                    }
                }

                String key = keyEvent.getCode().getName();

                if (key.equals("Left")) {
                    Pacman.getInstance().arrow = new Pair("left");
                } else if (key.equals("Right")) {
                    Pacman.getInstance().arrow = new Pair("right");
                } else if (key.equals("Up")) {
                    Pacman.getInstance().arrow = new Pair("up");
                } else if (key.equals("Down")) {
                    Pacman.getInstance().arrow = new Pair("down");
                }
            }
        };

        Page.stage.getScene().addEventFilter(KeyEvent.ANY, filter);
    }

    public void initPane() {

        pane = new Pane();
        pane.getChildren().add(Pacman.getInstance().board);
        for (roh roh : roh.rohs) {
            pane.getChildren().add(roh);
        }
        pane.getChildren().add(Pacman.getInstance());
    }


    public void setBoardPostion() {
        gridPaneX = (sceneX / 2 - (15 * Board.numColumn));
        Pacman.getInstance().board.setLayoutX(gridPaneX);
        gridPaneY = (sceneY / 2 - (15 * Board.numRow));
        Pacman.getInstance().board.setLayoutY(gridPaneY);

    }

    public void setPacmanAndRohsPosition() {

        for (roh roh : roh.rohs) {
            roh.initPosition(gridPaneX, gridPaneY);
            roh.stopWait = 2000L;
        }
        Pacman.getInstance().initPosition();
    }

    public void setScoreLabel() {
        pane.getChildren().add(scoreLabel);
        scoreLabel.setLayoutX(sceneX / 2);
        scoreLabel.setLayoutY(gridPaneY);
        scoreLabel.setText(score + "");
    }

    public void setLifeBox() {

        pane.getChildren().add(lifeBox);
        for (int i = 0; i < life; i++) {
            lifeBox.getChildren().add(new life());
        }
        lifeBox.setLayoutX(gridPaneX + 30);
        lifeBox.setLayoutY(gridPaneY + 30 * (numRow - 1) + 5);

    }

    public void setMessage() {

        pane.getChildren().add(message);
        message.setCenterShape(true);
        message.setLayoutX(sceneX / 2 - 90);
        message.setLayoutY(gridPaneY + 30 * (numRow - 1) + 5);
        message.getStyleClass().add("red");
    }

    public void increaseScore(int increasingScore) {
        score += increasingScore;
        scoreLabel.setText(score + "");
    }

    public static String getUsername() {
        if (user == null) {
            return "";
        }
        return user.getUsername();
    }

    public void askForGame() {

        Button gameButton = new Button();
        pane.getChildren().add(gameButton);
        gameButton.setLayoutX(sceneX / 2 + 20);
        gameButton.setLayoutY(sceneY / 2);
        gameButton.setMaxSize(50, 50);
        Image image = new Image("/pic/backArrow2.png");
        ImageView view = new ImageView(image);
        view.setFitHeight(50);
        view.setFitWidth(50);
        gameButton.setGraphic(view);

        Button cancelButton = new Button();
        pane.getChildren().add(cancelButton);
        cancelButton.setMinSize(50, 50);
        cancelButton.setText("exit game");
        cancelButton.setLayoutX(sceneX / 2 - 70);
        cancelButton.setLayoutY(sceneY / 2);

        gameButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                startGame(user, gamePage.life, Pacman.getInstance().board.id);
            }
        });

        cancelButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                finishGame(true);
                if (user == null) {
                    new FirstPage().run();
                } else {
                    new MainMenu().run(user);
                }
            }
        });
    }

    public void finishGame(boolean shouldFileDeleted){
        Page.stage.getScene().removeEventFilter(KeyEvent.ANY, filter);
        user = null;
        isGameInProgress = false;
        if(shouldFileDeleted){
            Data.deleteData();
        }
        stopAnimations();
    }

    public void continueGame() {

        decreaseLife();
        if (life < 0) {
            askForGame();
            return;
        }
        instance.isContinueGame = true;
        hasGameStarted = false;
        print("d");
        instance.setMessageText();
        play();
    }

    public static void startGame(User user, int life, int boardID) {
        game.user = user;
        instance = new game(boardID, life);
        instance.isContinueGame = false;
        for (roh roh : roh.rohs) {
            roh.interSectInvalidTimeWait = 2000L;
        }
        instance.setMessageText();
        play();
        stage.getScene().setRoot(instance.pane);
        roh.initRohsForStartNewGame();
        isGameInProgress = true;
    }

    public void decreaseLife() {

        game.instance.life--;

        if (game.instance.life >= 0) {
            lifeBox.getChildren().remove(lifeBox.getChildren().size() - 1);
        }
    }

    public void removeMessage() {
        message.setText("");
    }

    public void setMessageText() {
        message.setText("enter any arrow to start");
    }

    public void setButtons() {

        buttons = new HBox();
        buttons.getChildren().add(getBackButton());
        buttons.getChildren().add(getPauseButton());
        buttons.getChildren().add(Audio.getAudioButton());
        buttons.setSpacing(5);
        buttons.setLayoutX(0);
        buttons.setLayoutY(0);
        pane.getChildren().add(buttons);
    }

    public Button getBackButton(){

        Button backButton = new Button();
        Page.setBackButton(backButton);
        backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (user == null) {
                    finishGame(true);
                    new FirstPage().run();
                } else {
                    Data.saveData();
                    User tempUser = user;
                    finishGame(false);
                    new MainMenu().run(tempUser);
                }

            }
        });
        return backButton;
    }

    public Button getPauseButton(){

        Button pauseButton = new Button();

        ImageView playView = new ImageView(new Image("/pic/play.jpg"));
        playView.fitHeightProperty().bind(pauseButton.heightProperty());
        playView.fitWidthProperty().bind(pauseButton.widthProperty());

        ImageView pauseView = new ImageView(new Image("/pic/pause.png"));
        pauseView.fitHeightProperty().bind(pauseButton.heightProperty());
        pauseView.fitWidthProperty().bind(pauseButton.widthProperty());

        pauseButton.setGraphic(pauseView);
        pauseButton.setMinSize(40, 40);
        pauseButton.setMaxSize(40, 40);

        pauseButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Page.stage.getScene().setCursor(Cursor.HAND);
            }
        });

        pauseButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Page.stage.getScene().setCursor(Cursor.DEFAULT);
            }
        });
        pauseButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if (hasPaused) {
                    pauseButton.setGraphic(pauseView);
                    startGameByData();
                    Pacman.getInstance().requestFocus();
                } else {
                    Data.saveData();
                    pauseButton.setGraphic(playView);
                    stopAnimations();
                    hasPaused = true;
                }
            }
        });

        return pauseButton;
    }

}
