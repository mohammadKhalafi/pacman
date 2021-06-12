package sample;

import sample.models.User;
import sample.controller.DataBaseController;

public class RegisterAndLoginController extends view {

    private static RegisterAndLoginController instance;

    private RegisterAndLoginController() {
    }

    public static RegisterAndLoginController getInstance() {
        if (instance == null) return new RegisterAndLoginController();
        return instance;
    }

    public String register(String username, String password, String repeatedPassword) {


        if (username.equals("")) {
            return "you should enter username";
        }

        if (password.equals("")) {
            return "you should enter password";
        }

        if (!password.equals(repeatedPassword)) {
            print("passwords don't match");
            return "passwords don't match";
        }

        try {
            DataBaseController.createUser(username, password);
        } catch (Exception e) {
            e.printStackTrace();
            print(e.getMessage());
            return e.getMessage();
        }
        print("here 2");
        return null;
    }

    public String login(String username, String password) {

        print("login menu");

        if (username.equals("")) {
            return "you should enter your username";
        }

        if (password.equals("")) {
            return "you should enter your password";
        }

        User user = DataBaseController.getUserByUsername(username);
        if (user == null) {
            print("invalid username");
            return "invalid username";
        } else if (!user.getPassword().equals(password)) {
            print("incorrect password");
            return "incorrect password";
        }

        return "";
    }
}
