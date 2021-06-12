package sample.models;

public class Pair {

    @Override
    public String toString() {
        return i + " " + j;
    }

    public double i;
    public double j;
    public String arrowKey = "";

    public Pair(String arrowKey) {
        this.arrowKey = arrowKey;
        switch (arrowKey) {
            case "left" -> setIJ(-1, 0);
            case "right" -> setIJ(1, 0);
            case "up" -> setIJ(0, -1);
            case "down" -> setIJ(0, 1);
            default -> setIJ(0, 0);
        }
    }

    public Pair(double i, double j) {
        setIJ(i, j);
        if (i == -1 && j == 0) {
            arrowKey = "left";
        } else if (i == 1 && j == 0) {
            arrowKey = "right";
        } else if (i == 0 && j == 1) {
            arrowKey = "down";
        } else if (i == 0 && j == -1) {
            arrowKey = "up";
        }
    }

    public void setIJ(double i, double j) {
        this.i = i;
        this.j = j;
    }


}
