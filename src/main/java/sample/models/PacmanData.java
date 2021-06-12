package sample.models;

public class PacmanData {

    public double x;
    public double y;
    public String imageArrow;
    public String speedArrow;
    public long passedTimeOfPacmanTime;

    public PacmanData(double i, double j, double x, double y, String arrow, long passedTimeOfPacmanTime) {
        speedArrow = new Pair(i, j).arrowKey;
        this.x = x;
        this.y = y;
        this.imageArrow = arrow;
        this.passedTimeOfPacmanTime = passedTimeOfPacmanTime;
    }
}
