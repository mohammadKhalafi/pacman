package sample;

import javafx.scene.control.TextField;

public class Utils {

    public static void clearTextField(TextField... textFields) {
        for (TextField textField : textFields) {
            textField.clear();
        }
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
