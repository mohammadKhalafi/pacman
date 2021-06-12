package sample.models;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import sample.controller.Board;

import java.util.ArrayList;

public class roh extends Rectangle {

    public static int counter = 1;
    public static ArrayList<roh> rohs = new ArrayList<>();

    static {
        for (int i = 0; i < 4; i++) {
            rohs.add(new roh());
        }
    }

    int num;
    public Long stopStart = 0L;
    public Long stopWait = 0L;
    public Long remainingStopTimeForSeialize = 0L;

    public Long interSectInvalidTimeStart = 0L;
    public Long interSectInvalidTimeWait = 0L;
    public Long remainingInterSectInvalidTimeForSerialize = 0L;

    public boolean isPacmanTime = false;
    public RohTransition tr;
    {
        tr = new RohTransition(new Pair(1, 0));
        tr.setToop(this);
    }

    private roh() {
        super(20, 20);
        num = counter;
        setBackGroundImage();
        counter++;
    }

    public static void setStartTimes(){
        for(roh roh : rohs){
            roh.interSectInvalidTimeStart = System.currentTimeMillis();
            roh.stopStart = System.currentTimeMillis();
        }
    }

    public static void setPacmanTimeMode() {
        for (roh roh : rohs) {
            roh.setFill(new ImagePattern(new Image("/pic/5.png")));
            roh.isPacmanTime = true;
        }
    }


    public static void undoPacmanTime() {
        for (roh roh : rohs) {
            roh.setBackGroundImage();
            roh.isPacmanTime = false;
        }
    }

    public void setBackGroundImage() {
        setFill(new ImagePattern(new Image("/pic/" + num + ".png")));
    }

    public void move(double speed, double i, double j) {
        setLayoutX(getLayoutX() + i * speed);
        setLayoutY(getLayoutY() + j * speed);
    }

    public static void initRohsByData(Data data) {
        for (roh roh : rohs) {
            roh.initRohByData(data);
        }
    }

    public void initRohByData(Data data) {

        RohData rohData = data.rohDatas.get(num - 1);
        setLayoutX(rohData.x - 10);
        setLayoutY(rohData.y - 10);

        Pair speed = new Pair(rohData.speedArrow);

        tr.i = speed.i;
        tr.j = speed.j;
        tr.tempArrow = rohData.tempArrow;

        stopWait = Math.max(0, rohData.remainingTimeForStop);
        interSectInvalidTimeWait = Math.max(0, rohData.remainingTimeForInvalidIntersect);

        if(rohData.isPacmanTime){
            setFill(new ImagePattern(new Image("/pic/5.png")));
        }
    }

    public RohData getData() {
        return new RohData(getBoundsInParent().getCenterX(), getBoundsInParent().getCenterY(),
                new Pair(tr.i, tr.j).arrowKey, tr.tempArrow, isPacmanTime,
                stopWait - (System.currentTimeMillis() - stopStart),
                interSectInvalidTimeWait -
                        (System.currentTimeMillis() - interSectInvalidTimeStart));
    }

    public boolean IsIntersectValid() {
        if (System.currentTimeMillis() - interSectInvalidTimeStart >= interSectInvalidTimeWait)
            return true;
        return false;
    }

    public void initPosition(int gridPaneX, int gridPaneY) {

        if (num == 1) {
            setLayoutX(gridPaneX + 5 + 30);
            setLayoutY(gridPaneY + 5 + 30);
        } else if (num == 2) {
            setLayoutX(gridPaneX + 5 + 30 * (Board.numColumn - 2));
            setLayoutY(gridPaneY + 5 + 30);
        } else if (num == 3) {
            setLayoutX(gridPaneX + 5 + 30);
            setLayoutY(gridPaneY + 5 + 30 * (Board.numRow - 2));
        } else if (num == 4) {
            setLayoutX(gridPaneX + 5 + 30 * (Board.numColumn - 2));
            setLayoutY(gridPaneY + 5 + 30 * (Board.numRow - 2));
        }
    }

    public static void initRohsForStartNewGame(){
        for(roh roh : rohs){
            roh.initSomeRohFieldsForStartNewGame();
        }
    }
    public void initSomeRohFieldsForStartNewGame() {
        stopStart = 0L;
        stopWait = 0L;
        remainingStopTimeForSeialize = 0L;
        interSectInvalidTimeStart = 0L;
        interSectInvalidTimeWait = 0L;
        remainingInterSectInvalidTimeForSerialize = 0L;
        isPacmanTime = false;
        setBackGroundImage();
    }
}
