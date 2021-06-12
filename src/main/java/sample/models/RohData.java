package sample.models;

public class RohData {

    public double x;
    public double y;
    public String speedArrow;
    public String tempArrow;
    public long remainingTimeForStop;
    public long remainingTimeForInvalidIntersect;
    public boolean isPacmanTime;

    public RohData(double x, double y, String speedArrow, String tempArrow,
                   boolean isPacmanTime, long remainingTimeForStop, long remainingTimeForInvalidIntersect) {

        this.x = x;
        this.y = y;
        this.speedArrow = speedArrow;
        this.tempArrow = tempArrow;
        this.isPacmanTime = isPacmanTime;
        this.remainingTimeForStop = remainingTimeForStop;
        this.remainingTimeForInvalidIntersect = remainingTimeForInvalidIntersect;
    }
}
