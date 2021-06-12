package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import sample.controller.Audio;
import sample.controller.Page;
import sample.controller.game;
import sample.models.Data;
import sample.models.User;
import sample.controller.DataBaseController;

import java.io.*;
import java.util.Optional;

import static sample.view.print;

public class MainMenu extends Page {

    private static User user;
    public TextFlow username;
    public TextFlow score;
    public Button picButton;
    public Label buttonText;
    public Button scoreBoardButton;
    public Button gameButton;
    public Button audioButton;

    public void run(User user) {

        MainMenu.user = user;

        try {
            Parent root = FXMLLoader.load(getClass().getResource("fxml/MainMenu.fxml"));
            stage.getScene().setRoot(root);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @FXML
    public void initialize() {

        initTexts();
        initPicButton();
        initGameButton();
        initScoreBoardButton();
        initAudioButton();
    }

    public void initAudioButton() {
        Audio.setAudioButton(audioButton);
    }

    public void initPicButton() {
        picButton.setMinSize(150, 200);

        picButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                buttonText.setText("");
            }
        });

        final String text;
        final int font;
        if (user.getImagePath() == null) {
            text = "set your profile image";
            font = 13;
        } else {
            text = "change it !";
            font = 18;
        }

        picButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                buttonText.setText(text);
                buttonText.setTextFill(Color.BLUE);
                buttonText.setFont(Font.font(font));
                buttonText.setMinWidth(8);
            }
        });

        if (user.getImagePath() != null) {
            setButtonImage(new File(user.getImagePath()));
        }
    }

    public void initTexts() {
        Text text1 = new Text("username : ");
        text1.setId("red2");

        Text text2 = new Text(user.getUsername());
        text2.setId("blue2");
        username.getChildren().addAll(text1, text2);

        Text text3 = new Text("score : ");
        text3.setId("red2");

        Text text4 = new Text(user.getScore() + "");
        text4.setId("blue2");

        score.getChildren().addAll(text3, text4);
    }

    public void initGameButton() {

        gameButton.setShape(new Circle(20));
        gameButton.setMinSize(70, 70);
        gameButton.setText("game");
        gameButton.setTextFill(Color.RED);
        gameButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Data data = Data.getLastData();
//                print(data != null , user.getUsername().equals(data.username) , askForNewGame());
                if (data != null && user.getUsername().equals(data.username) && askForNewGame()) {
                    game.startGameByData();
                } else {
                    new gamePage().run(user);
                }
            }
        });
    }

    public void initScoreBoardButton() {
        scoreBoardButton.setText("score board");
        scoreBoardButton.setMinSize(70, 70);
        scoreBoardButton.setShape(new Circle(20));
        scoreBoardButton.setTextFill(Color.RED);
        scoreBoardButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                new ScoreBoard().run(user);
            }
        });
    }

    public boolean askForNewGame() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to continue last game"
                , ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.YES;

    }

    public void handleLogout(ActionEvent actionEvent) {
        showPopupMessage("you successfully loged out");
        new FirstPage().run();
    }

    public void changePassword(ActionEvent actionEvent) {

        BorderPane mainPane = new BorderPane();
        mainPane.setId("pane");

        VBox box = new VBox();
        box.setSpacing(10);
        box.setAlignment(Pos.CENTER);

        HBox buttonsBox = new HBox();
        Button backButton = new Button();
        RegisterMenu.setBackButton(backButton);
        backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                run(user);
            }
        });
        buttonsBox.getChildren().add(backButton);

        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setVgap(10);
        pane.setHgap(10);

        TextField oldPasswordField = new TextField();
        TextField newPasswordField = new TextField();
        TextField newPasswordRepeatField = new TextField();

        Label oldPasswordLabel = new Label("password : ");
        Label newPasswordLabel = new Label("new password :");
        Label newPasswordRepeatLabel = new Label("confirm new password :");
        Label error = new Label();
        error.getStyleClass().add("red");

        pane.add(oldPasswordLabel, 0, 0);
        pane.add(oldPasswordField, 1, 0);
        pane.add(newPasswordLabel, 0, 1);
        pane.add(newPasswordField, 1, 1);
        pane.add(newPasswordRepeatLabel, 0, 2);
        pane.add(newPasswordRepeatField, 1, 2);

        Button changePasswordButton = new Button("change password");
        changePasswordButton.setMaxSize(220, 10);
        changePasswordButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                onClick(oldPasswordField, newPasswordField, newPasswordRepeatField,
                        error);
            }
        });


        box.getChildren().add(pane);
        box.getChildren().add(changePasswordButton);
        box.getChildren().add(error);

        mainPane.setTop(buttonsBox);

        mainPane.setCenter(box);
        stage.getScene().setRoot(mainPane);
    }

    private void onClick(TextField oldPasswordTF, TextField newPassword1TF, TextField newPassword2TF, Label error) {

        String oldPassword = oldPasswordTF.getText();
        String newPassword1 = newPassword1TF.getText();
        String newPassword2 = newPassword2TF.getText();

        String result = new profileController().changePassword(user, oldPassword, newPassword1, newPassword2);

        if (result.equals("")) {
            showPopupMessage("password changed successfully");
            run(user);
            return;
        }

        if (oldPasswordTF.getText().equals("") ||
                newPassword1TF.getText().equals("") || newPassword2TF.getText().equals("")) {
        } else {
            Utils.clearTextField(oldPasswordTF, newPassword1TF, newPassword2TF);
        }

        error.setText(result);
    }


    public void deleteAccount(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("if you are sure to delete your account press ok or press cancel");
        alert.setTitle("deleting account conformation");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() != ButtonType.OK) {
            showPopupMessage("OK!");
            return;
        }

        try {
            DataBaseController.deleteUser(user.getUsername());
        } catch (Exception e) {
            print(e.getMessage());
        }
        showPopupMessage("you successfully deleted your account");
        new FirstPage().run();
    }


    @FXML
    public void choseFile(MouseEvent mouseEvent) {

        File file = new FileChooser().showOpenDialog(stage);
        print(file.getAbsolutePath());
        setButtonImage(file);

    }

    public void setButtonImage(File file) {

        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            showPopupMessage("invalid file");
            return;
        }

        Image img = new Image(inputStream);

        if (img.isError()) {
            showPopupMessage("invalid file");
            return;
        }

        ImageView view = new ImageView();

        view.setImage(img);
        view.setFitWidth(150);
        view.setFitHeight(200);

        boolean wasNull = user.getImagePath() == null;
        user.setImagePath(file.getPath());
        DataBaseController.upDateUser(user);

        picButton.setGraphic(view);
        picButton.getGraphic().setPickOnBounds(true);

        if (wasNull) {
            picButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                    buttonText.setText("change this !");
                    buttonText.setTextFill(Color.BLUE);
                    buttonText.setFont(Font.font(18));
                    buttonText.setMinWidth(8);
                }
            });
        }
    }
}
