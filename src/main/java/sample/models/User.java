package sample.models;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static sample.view.print;

public class User {
    private String username;
    private String password;
    protected int score = 0;
    private String imagePath;
    private ArrayList<Integer> maps = new ArrayList<>();

    private String lastScoreUpdate = LocalDateTime.of(1, 1, 1, 1, 1, 1, 1).toString();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LocalDateTime getLastScoreUpdate() {
        return LocalDateTime.parse(lastScoreUpdate);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void increaseScore(int increasingScore) {
        this.score += increasingScore;
        lastScoreUpdate = LocalDateTime.now().toString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void addMap(int id) {
        maps.add(id);
    }

    public ArrayList<Integer> getMaps() {
        return maps;
    }

}
