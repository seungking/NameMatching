package com.e.namematching.model;

public class GameData {
    private int stage; //스테이지횟수
    private int score; // 스코어
    private int hp; //기회
    private int show; //한판마다 볼 수 있는 횟수
    private int chance; //스킵 횟수
    private int hp_chance; //광고 1회 보고 hp 회복
    private int show_change; //광고 1회 보고 볼 수 있는 횟수 2회 증가

    public GameData() {
        this.stage = 0;
        this.score = 0;
        this.hp = 3;
        this.show = 0;
        this.chance = 1;
        this.hp_chance = 1;
        this.show_change = 1;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public int getHp_chance() {
        return hp_chance;
    }

    public void setHp_chance(int hp_chance) {
        this.hp_chance = hp_chance;
    }

    public int getShow_change() {
        return show_change;
    }

    public void setShow_change(int show_change) {
        this.show_change = show_change;
    }
}
