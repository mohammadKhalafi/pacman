package sample.controller;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sample.FirstPage;
import sample.models.Cell;
import sample.models.Data;
import sample.models.Pacman;
import sample.models.Pair;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static sample.view.print;

public class Board extends GridPane {


    public static int numRow = 19;
    public static int numColumn = 25;

    public int numCoins = 0;
    public int id = -1;

    private Board() {
    }

    public static boolean isNotNextCellWall(Pair pair, Node node) {

        Pacman ball = Pacman.getInstance();

        double X = node.getBoundsInParent().getCenterX();
        double Y = node.getBoundsInParent().getCenterY();


        int J = (int) (GridPane.getRowIndex(getCell(X, Y)) + pair.j);
        int I = (int) (GridPane.getColumnIndex(getCell(X, Y)) + pair.i);


        if (((Cell) ball.board.getChildren().get(getIndex(I, J))).model != 1) {
            return false;
        }

        return true;
    }

    private static Cell getCell(GridPane board, int i, int j) {
        return ((Cell) board.getChildren().get(getIndex(i, j)));
    }

    public static int getIndex(int i, int j) {
        return i * numRow + j;
    }

    private static int getModel(int i, int j) {

        if (i == 0 || i == numColumn - 1 || j == 0 || j == numRow - 1) {
            return 3;
        }
        if ((i == 2 || i == numColumn - 3) && j % 2 == 0) {
            return 2;
        }
        return 1;
    }

    public static int getNextCellModel(Pair pair, Node node) {

        Pacman ball = Pacman.getInstance();

        double X = node.getBoundsInParent().getCenterX();
        double Y = node.getBoundsInParent().getCenterY();


        int J = (int) (GridPane.getRowIndex(getCell(X, Y)) + pair.j);
        int I = (int) (GridPane.getColumnIndex(getCell(X, Y)) + pair.i);


        return ((Cell) ball.board.getChildren().get(getIndex(I, J))).model;

    }


    public static boolean isArrowValid() {
        if (!isSpecialPoint(Pacman.getInstance())) {
            return false;
        }
        return isNotNextCellWall(new Pair(Pacman.getInstance().arrow.arrowKey), Pacman.getInstance());
    }

    public static boolean isSpecialPoint(Node node) {

        double X = node.getBoundsInParent().getCenterX();
        double x = (X - (game.sceneX / 2 % 30)) % 30;

        double Y = node.getBoundsInParent().getCenterY();
        double y = (Y - (game.sceneY / 2 % 30)) % 30;

        if (x > 0.1) {
            return false;
        }

        if (y > 0.01) {
            return false;
        }
        return true;
    }

    public static Node getCell(double x, double y) {

        int i = (int) Math.ceil((x - (game.sceneX / 2 - Board.numColumn * 15))) / 30;
        int j = (int) Math.ceil((y - (game.sceneY / 2 - Board.numRow * 15))) / 30;


        return Pacman.getInstance().board.getChildren().get(getIndex(i, j));
    }

    public static Board getBoardById(int id) {
        String data = DataBaseController.getDataOfFile(DataBaseController.getMapPathByIndex(id));
        Board board = getBoard(data);
        board.id = id;
        return board;
    }

    public static Board getBoard(String data) {

        Board board = new Board();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> cellsData = new Gson().fromJson(data, type);

        for (int i = 0; i < numColumn; i++) {
            for (int j = 0; j < numRow; j++) {
                Cell cell = Cell.deserialize(cellsData.get(getIndex(i, j)));
                if (cell.coin != null || cell.bomb != null) {
                    board.numCoins++;
                }
                board.add(cell, i, j);
            }
        }

        return board;
    }

    public static int getRandomBoard() {

        Board board = new Board();
        if (new Random().nextInt() % 2 == 0) {
            board.create1();
        } else {
            board.create2();
        }

        board.setCellsCoin();
        board.setCellsBomb();

        return board.saveBoard();
    }


    private void create2() {
        randBoard.createArray();

        for (int i = 0; i < numColumn; i++) {
            for (int j = 0; j < numRow; j++) {
                if (getModel(i, j) == 3) {
                    this.add(new Cell(3), i, j);
                } else if (randBoard.array[j][i] == 0) {
                    this.add(new Cell(1), i, j);
                } else {
                    this.add(new Cell(2), i, j);
                }

            }
        }
    }

    private void create1() {

        for (int i = 0; i < numColumn; i++) {
            for (int j = 0; j < numRow; j++) {

                this.add(new Cell(getModel(i, j)), i, j);
            }
        }

        for (int j = 2; j < numRow - 2; j++) {
            for (int i = 2; i < numColumn - 2; i++) {

                Cell cell = getCell(this, i, j);
                if (j % 2 == 0) {

                    int model;
                    if (getCell(this, i - 1, j).model == 2) {

                        model = Math.abs(new Random().nextInt() % 2) + 1;
                        if (model != 1) {
                            cell.setModel(2);
                        }
                    } else {
                        cell.setModel(2);
                    }

                }
            }
        }

    }

    private void setCellsCoin() {

        for (Node node : this.getChildren()) {
            Cell cell = (Cell) node;

            if (cell.model == 1) {
                cell.setCoin();
                numCoins++;
            }
        }


        Cell cell = (Cell) this.getChildren().get(getIndex(numColumn / 2, numRow / 2));
        cell.removeCoin();
        numCoins--;
    }

    private void setCellsBomb() {

        for (Node node : this.getChildren()) {
            Cell cell = (Cell) node;

            if (cell.model == 1 && !(GridPane.getRowIndex(cell) == numRow / 2 &&
                    GridPane.getColumnIndex(cell) == numColumn / 2)) {

                if (new Random().nextInt() % 50 == 0) {
                    cell.setBomb();
                }
            }
        }
    }

    public String getData() {

        ArrayList<String> cellsData = new ArrayList<>();

        for (Node node : getChildren()) {
            Cell cell = (Cell) node;
            cellsData.add(cell.getData());
        }
        Gson gson = new GsonBuilder().create();
        return gson.toJson(cellsData);
    }

    public int saveBoard() {
        int ans = DataBaseController.getNewMapIndex();
        DataBaseController.createFile(
                DataBaseController.getMapsPath() + "/" + ans + ".json",
                getData());

        return ans;
    }


}



