package com.e.namematching.model;

public class GameData {
    private int stage;
    private int hp;
    private int show;
    private int chance;
    private int hp_chance;
    private int show_change;

    public GameData(int stage, int hp, int show, int chance, int hp_chance, int show_change) {
        this.stage = stage;
        this.hp = hp;
        this.show = show;
        this.chance = chance;
        this.hp_chance = hp_chance;
        this.show_change = show_change;
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
