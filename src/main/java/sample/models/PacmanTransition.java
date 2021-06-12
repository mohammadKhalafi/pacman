package sample.models;

import javafx.animation.Transition;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import sample.controller.*;

import java.util.ArrayList;

public class PacmanTransition extends Transition {

    int speed = 2;
    public double i;
    public double j;
    public long pacmanTimeStart = 0L;
    public long passedTimeForSeialize = 10000L;
    public int numEated = 0;


    public PacmanTransition(Pair Pair) {
        setCycleCount(-1);
        setCycleDuration(Duration.millis(100));
        this.i = Pair.i;
        this.j = Pair.j;
    }

    public void setIJ(Pair Pair) {
        this.i = Pair.i;
        this.j = Pair.j;
    }


    private ArrayList<Cell> getNearCells() {

        ArrayList<Cell> cells = new ArrayList<>();

        Pacman pacman = Pacman.getInstance();

        double X = pacman.getBoundsInParent().getCenterX();
        double Y = pacman.getBoundsInParent().getCenterY();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Cell cell = (Cell) pacman.board.getChildren().get(Board.getIndex(
                        i + GridPane.getColumnIndex(Board.getCell(X, Y)) - 1,
                        j + GridPane.getRowIndex(Board.getCell(X, Y)) - 1));

                cells.add(cell);
            }
        }

        return cells;
    }


    private void bombHandling(Cell cell) {
        cell.removeBomb();
        pacmanTimeStart = System.currentTimeMillis();
        numEated = 0;
    }

    private boolean checkCoins(ArrayList<Cell> cells) {

        for (Cell cell : cells) {

            boolean bool = false;

            if (cell.bomb != null) {
                Shape intersect = Shape.intersect(Pacman.getInstance(), cell.bomb);
                if (intersect.getBoundsInParent().getWidth() > 0) {
                    bombHandling(cell);
                    roh.setPacmanTimeMode();
                    Audio.playChomp();
                    bool = true;
                }
            } else if (cell.coin != null) {

                Shape intersect = Shape.intersect(Pacman.getInstance(), cell.coin);
                if (intersect.getBoundsInParent().getWidth() > 0) {
                    Audio.playChomp();
                    cell.removeCoin();
                    if (game.user != null) {
                        game.user.increaseScore(5);
                        game.instance.increaseScore(5);
                        DataBaseController.upDateUser(game.user);
                    } else {
                        game.instance.increaseScore(5);
                    }
                    bool = true;
                }
            }

            if (bool) {

                int numCoins = --Pacman.getInstance().board.numCoins;
                if (numCoins == 0) {
                    return true;
                }
                break;
            }
        }
        return false;
    }


    @Override
    protected void interpolate(double v) {

        if (pacmanTimeStart != 0L && System.currentTimeMillis() - pacmanTimeStart > 10000L) {
            pacmanTimeStart = 0L;
            numEated = 0;
            roh.undoPacmanTime();
        }

        Pacman ball = Pacman.getInstance();

        if (!game.instance.hasGameStarted) {
            return;
        }

        if (checkCoins(getNearCells())) {
            game.stopAnimations();
            game.startGame(game.user, game.instance.life + 1, Pacman.getInstance().board.id);
            return;
        }

        for (roh roh : roh.rohs) {

            if (roh.getBoundsInParent().intersects(Pacman.getInstance().getBoundsInParent())) {

                if (roh.IsIntersectValid()) {

                    if (System.currentTimeMillis() - pacmanTimeStart <= 100000L && roh.isPacmanTime) {
                        Audio.playEatGhost();
                        roh.initPosition(game.gridPaneX, game.gridPaneY);
                        roh.setBackGroundImage();
                        numEated++;
                        roh.interSectInvalidTimeWait = 5000L;
                        roh.interSectInvalidTimeStart = System.currentTimeMillis();
                        roh.stopStart = System.currentTimeMillis();
                        roh.stopWait = 5000L;
                        roh.isPacmanTime = false;

                        if (game.user != null) {
                            game.user.increaseScore(200 * numEated);
                            DataBaseController.upDateUser(game.user);
                        }
                        game.instance.increaseScore(200 * numEated);

                    } else {
                        Audio.playPacMacDeath();
                        game.stopAnimations();
                        game.instance.continueGame();
                        return;
                    }
                }
            }
        }


        if (ball.arrow.arrowKey.equals("")) {

            if (Board.isSpecialPoint(Pacman.getInstance())) {
                if (!Board.isNotNextCellWall(new Pair(i, j), Pacman.getInstance())) {
                    setIJ(new Pair(0, 0));
                }
            }

        } else {
            Pair arrowkey = new Pair(ball.arrow.arrowKey);

            if (arrowkey.i + i == 0 && arrowkey.j + j == 0) {
                setIJ(ball.arrow);
                ball.arrow = new Pair("");
            } else {
                if (Board.isArrowValid()) {
                    setIJ(ball.arrow);
                    ball.arrow = new Pair("");
                }
            }
        }

        ball.move(speed, i, j);
    }
}
