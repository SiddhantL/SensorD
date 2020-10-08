package com.example.runningman;
public class ListData {
    private String name,key;
    private int score;

    public  ListData(){
    }

    public ListData(String name,int score,String key) {
        this.score=score;
        this.name = name;
        this.key=key;
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
    public String getKeys() {
        return key;
    }
    public void setScore(int score) {
        this.score=score;
    }
}

