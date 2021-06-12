package sample.models;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import sample.controller.Board;
import sample.controller.game;

import static sample.view.print;

public class Pacman extends Circle {

    public Pair arrow = new Pair("");

    public Board board;

    public PacmanTransition tr = new PacmanTransition(new Pair(""));
    public pacmanTransition2 tr2 = new pacmanTransition2();

    static Pacman instance;

    public Pacman() {

        super(12, Color.rgb(130, 236, 90));

        setFill(new ImagePattern(new Image("/pic/pacman2_right.png")));

    }

    public static Pacman getInstance() {
        if (instance == null) {
            instance = new Pacman();
        }
        return instance;
    }

    public static void initPacmanByData(Data data) {

        instance = new Pacman();
        instance.board = Board.getBoard(data.BoardData);
        instance.board.id = data.boardID;
        instance.tr2.arrow = data.pacmanData.imageArrow;
        instance.setCenterX(data.pacmanData.x);
        instance.setCenterY(data.pacmanData.y);
        instance.tr.passedTimeForSeialize = data.pacmanData.passedTimeOfPacmanTime;
        print(instance.tr.passedTimeForSeialize);
        Pair speed = new Pair(data.pacmanData.speedArrow);
        instance.tr.i = speed.i;
        instance.tr.j = speed.j;

    }

    public void move(int speed, double i, double j) {

        if (Board.isSpecialPoint(Pacman.getInstance())) {
            if (!Board.isNotNextCellWall(new Pair(i, j), Pacman.getInstance())) {
                tr.setIJ(new Pair(0, 0));
                return;
            }
        }

        setCenterX(getCenterX() + i * speed);
        setCenterY(getCenterY() + j * speed);
    }

    public PacmanData getData() {

        long passedTime = Math.min(10000L, System.currentTimeMillis() - tr.pacmanTimeStart);

        return new PacmanData(tr.i, tr.j, instance.getBoundsInParent().getCenterX(),
                instance.getBoundsInParent().getCenterY(),
                instance.tr2.arrow, passedTime);
    }

    public void initPosition() {
        setCenterX(game.sceneX / 2);
        setCenterY(game.sceneY / 2);
    }

}


