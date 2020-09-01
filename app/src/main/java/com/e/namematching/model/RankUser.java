package com.e.namematching.model;

import android.graphics.Bitmap;

public class RankUser {

    private String name;
    private int score;
    private Bitmap flag;

    public RankUser(String name, int score, Bitmap flag) {
        this.name = name;
        this.score = score;
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Bitmap getFlag() {
        return flag;
    }

    public void setFlag(Bitmap flag) {
        this.flag = flag;
    }
}
