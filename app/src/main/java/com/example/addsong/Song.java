package com.example.addsong;

public class Song {
    private String title;
    private String des;

    public Song() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Song(String title, String des) {
        this.title = title;
        this.des = des;
    }
}
