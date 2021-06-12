package sample.models;

import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;
import sample.controller.Page;
import sample.controller.game;

import static sample.view.print;

public class pacmanTransition2 extends Transition {

    public String arrow = "right";

    public pacmanTransition2(){
        setCycleCount(-1);
        setCycleDuration(Duration.millis(500));
    }
    @Override
    protected void interpolate(double v) {

        if(!game.instance.hasGameStarted) {
            Pacman.getInstance().setFill(new ImagePattern(new Image("/pic/pacman2_" + arrow + ".png")));
        } else if(Pacman.getInstance().tr.i == 0 && Pacman.getInstance().tr.j == 0){
            Pacman.getInstance().setFill(new ImagePattern(new Image("/pic/pacman2_" + arrow + ".png")));
        }else{
            arrow = new Pair(Pacman.getInstance().tr.i, Pacman.getInstance().tr.j ).arrowKey;
            int frame = (v * 2) <= 1 ? 1 : 3;
            Pacman.getInstance().setFill(new ImagePattern(new Image("/pic/pacman" + frame + "_" +
                    arrow + ".png")));
        }
    }
}
