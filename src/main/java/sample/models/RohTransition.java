package sample.models;

import javafx.animation.Transition;
import javafx.util.Duration;
import sample.controller.Board;
import sample.controller.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class RohTransition extends Transition {

    String tempArrow = "up";

    int moves = 0;
    public int speed = 1;

    public double i;
    public double j;
    public roh toop;

    public void setToop(roh toop) {
        this.toop = toop;
    }

    public RohTransition(Pair Pair) {

        setCycleCount(-1);
        setCycleDuration(Duration.millis(30));
        this.i = Pair.i;
        this.j = Pair.j;
    }

    public void setIJ(Pair Pair) {
        this.i = Pair.i;
        this.j = Pair.j;
    }

    @Override
    protected void interpolate(double v) {

        if (!game.instance.hasGameStarted) {
            return;
        }

        if (System.currentTimeMillis() - toop.stopStart <= toop.stopWait) {
            return;
        }

        if (Board.isSpecialPoint(toop)) {

            if (!Board.isNotNextCellWall(new Pair(i, j), toop)) {
                setIJ(new Pair(0, 0));
                setTempArrow();
            }

            if (Board.isNotNextCellWall(new Pair(tempArrow), this.toop)) {
                setIJ(new Pair(tempArrow));
            }

        }

        toop.move(speed, i, j);

        moves = (moves + 1) % (5 * 30 / speed);

        if (moves == 0) {
            setTempArrow();
        }

    }


    ArrayList<String> arrows = new ArrayList<>();

    {
        arrows.add("left");
        arrows.add("right");
        arrows.add("up");
        arrows.add("down");
    }


    private void setTempArrow() {

        if (i == 0 && j == 0) {
            setArrow0();

        } else {
            setArrow1();
        }
    }

    private void setArrow0() {

        Collections.shuffle(arrows);

        for (String arrow : arrows) {
            if (Board.isNotNextCellWall(new Pair(arrow), this.toop)) {
                tempArrow = arrow;
                break;
            }
        }
    }

    private void setArrow1() {

        String arrow1 = "";
        String arrow2 = "";

        for (int k = 0; k < 4; k++) {

            if (arrows.get(k).equals(new Pair(i, j).arrowKey)) {
                continue;
            }

            Pair pair = new Pair(arrows.get(k));

            if (pair.i + i == 0 && pair.j + j == 0) {
                continue;
            }
            if (arrow1.equals("")) {
                arrow1 = arrows.get(k);
            } else if (arrow2.equals("")) {
                arrow2 = arrows.get(k);
            } else {
                break;
            }
        }


        if (Board.getNextCellModel(new Pair(arrow1), this.toop) == 3) {
            tempArrow = arrow2;
        } else if (Board.getNextCellModel(new Pair(arrow2), this.toop) == 3) {
            tempArrow = arrow1;
        } else {
            if (new Random().nextBoolean()) {
                tempArrow = arrow2;
            } else {
                tempArrow = arrow1;
            }
        }


    }
}