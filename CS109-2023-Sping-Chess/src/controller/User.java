package controller;

import java.io.Serializable;

public class User implements Serializable {
    String name;
    private int Win=0;
    private int Score;
    public User(String name){
        this.name=name;
    }

    public int getScore() {
        return Score;
    }

    public int getWin() {
        return Win;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        Score = score;
    }

    public void setWin(int win) {
        Win = win;
    }

    public String getName() {
        return name;
    }

}
