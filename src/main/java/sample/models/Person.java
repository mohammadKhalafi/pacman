package sample.models;

import static sample.view.print;

public class Person extends User {

    private int rank;

    public Person(User user, int rank) {
        super(user.getUsername(), user.getPassword());
        this.rank = rank;
        setScore(user.score);
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

}
