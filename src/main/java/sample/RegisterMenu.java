package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import sample.controller.Audio;
import sample.controller.Page;


public class RegisterMenu extends Page {

    public TextField password;
    public TextField username;
    public TextField repeatedPassword;
    public Label Error;
    public Button backButton;
    public Button audioButton;

    @FXML
    public void initialize(){
        setBackButton(backButton);
        Audio.setAudioButton(audioButton);
    }

    @FXML
    public void onClick(MouseEvent e){

        String registerResult = RegisterAndLoginController.getInstance().register(username.getText(), password.getText(), repeatedPassword.getText());
        if(registerResult != null){
            Error.setText(registerResult);
            return;
        }
        FirstPage.showPopupMessage("you signed up successfully");
        new LoginMenu().run();
    }


    public void ClickBack(MouseEvent mouseEvent) {
        new FirstPage().run();
    }

}
