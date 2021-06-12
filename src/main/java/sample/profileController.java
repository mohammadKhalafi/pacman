package sample;

import sample.models.User;
import sample.controller.DataBaseController;

import static sample.view.print;

public class profileController {


    public String changePassword(User user, String oldPassword, String newPassword, String repeatedPassword) {

        if(oldPassword.equals("")){
            return "please enter your password";
        }
        if(newPassword.equals("")){
            return "please enter new password";
        }
        if(repeatedPassword.equals("")){
            return "please confirm new password";
        }


        if (!user.getPassword().equals(oldPassword)) {
            print("incorrect password");
            return "incorrect password";
        }
        if (!newPassword.equals(repeatedPassword)) {
            print("passwords don't match");

            return "passwords don't match";
        }
        user.setPassword(newPassword);

        DataBaseController.upDateUser(user);

        return "";
    }
}
