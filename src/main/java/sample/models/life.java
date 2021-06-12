package sample.models;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class life extends Circle {

    public life(){
        super(10);
        setFill(new ImagePattern(new Image("/pic/life.png")));
    }
}
