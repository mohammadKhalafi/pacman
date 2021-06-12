package sample;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sample.controller.Audio;
import sample.controller.DataBaseController;
import sample.controller.Page;

public class LoginMenu extends Page {


    private Label loginError;
    private TextField usernameField;
    private TextField passwordField;
    private BorderPane borderPane = new BorderPane();
    private GridPane gridPane = new GridPane();
    private VBox loginBox = new VBox(10);

    public void run() {

        borderPane.setId("pane");
        setButtons();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(20);
        gridPane.setHgap(20);
        addFormToGridPane();
        setLoginBox();
        borderPane.setCenter(loginBox);
        stage.getScene().setRoot(borderPane);
    }


    private void setLoginBox(){

        Button loginButton = new Button();
        loginButton.setText("login");
        loginButton.setMaxSize(220, 10);
        loginButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                onClick();
            }
        });

        loginError = new Label();
        loginError.getStyleClass().add("red");
        loginError.setAlignment(Pos.CENTER);

        loginBox.setAlignment(Pos.CENTER);
        loginBox.getChildren().add(gridPane);
        loginBox.getChildren().add(loginButton);
        loginBox.getChildren().add(loginError);
    }

    private void addFormToGridPane(){

        usernameField = new TextField();
        passwordField = new TextField();
        Label usernameLabel = new Label();
        usernameLabel.setText("username :");
        Label passwordLabel = new Label();
        passwordLabel.setText("password :");
        Label welcomeLabel = new Label("Login");
        welcomeLabel.getStyleClass().add("yellow");

        gridPane.add(welcomeLabel, 0, 0);
        gridPane.add(usernameLabel, 0, 1);
        gridPane.add(usernameField, 1, 1);
        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordField, 1, 2);
    }

    private void setButtons(){

        HBox box = new HBox(5);
        Button backButton = new Button();
        setBackButton(backButton);
        backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                new FirstPage().run();
            }
        });
        box.getChildren().add(backButton);
        box.getChildren().add(Audio.getAudioButton());
        borderPane.setTop(box);
    }

    private void onClick() {

        String loginResult;
        String username = usernameField.getText();
        String password = passwordField.getText();

        loginResult = RegisterAndLoginController.getInstance().login(username, password);
        if (loginResult.equals("")) {
            showPopupMessage("hello " + username);
            new MainMenu().run(DataBaseController.getUserByUsername(username));
        } else {

            if (username.equals("") || password.equals("")) {

            } else {
                usernameField.clear();
                passwordField.clear();
            }

            loginError.setText(loginResult);
        }
    }
}
