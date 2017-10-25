package com.gmail.paulovitormelila.musicbuddy;

/**
 * Created by Paulo on 24/10/2017.
 */

public class Music {
    private String Name;
    private String Type;
    private String wTeaser; // description
    private String wUrl; // Wikipedia URL
    private String yUrl; // Youtube clip URL
    private String yID; // youtube clip ID

    public Music(String name, String type, String wTeaser, String wUrl, String yUrl, String yID) {
        Name = name;
        Type = type;
        this.wTeaser = wTeaser;
        this.wUrl = wUrl;
        this.yUrl = yUrl;
        this.yID = yID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getwTeaser() {
        return wTeaser;
    }

    public void setwTeaser(String wTeaser) {
        this.wTeaser = wTeaser;
    }

    public String getwUrl() {
        return wUrl;
    }

    public void setwUrl(String wUrl) {
        this.wUrl = wUrl;
    }

    public String getyUrl() {
        return yUrl;
    }

    public void setyUrl(String yUrl) {
        this.yUrl = yUrl;
    }

    public String getyID() {
        return yID;
    }

    public void setyID(String yID) {
        this.yID = yID;
    }
}


