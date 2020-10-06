package com.example.runningman;
public class ListData {
    private String name;
    private int score;

    public  ListData(){
    }

    public ListData(String name,int score) {
        this.score=score;
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public int getScore() {
        return score;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score=score;
    }
}

