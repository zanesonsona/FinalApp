package com.example.finalapp;


public class Events {

    private int image;
    private String month;
    private String title;
    private String day;
    private String location;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Events(int image, String month, String title, String day, String location) {
        this.image = image;
        this.month = month;
        this.title = title;
        this.day = day;
        this.location = location;
    }
}