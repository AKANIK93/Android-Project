package com.example.examapp.Models;

public class ResultModel {

    private String name;
    private int score;
    private int rank;

    public ResultModel(String name,int score, int rank) {
        this.score = score;
        this.rank = rank;
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
