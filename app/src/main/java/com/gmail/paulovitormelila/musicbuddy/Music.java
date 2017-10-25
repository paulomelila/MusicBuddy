package com.gmail.paulovitormelila.musicbuddy;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Paulo on 24/10/2017.
 */

public class Music implements Parcelable {
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

    private Music (Parcel in) {
        Name = in.readString();
        Type = in.readString();
        wTeaser = in.readString();
        wUrl = in.readString();
        yUrl = in.readString();
        yID = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Name);
        parcel.writeString(Type);
        parcel.writeString(wTeaser);
        parcel.writeString(wUrl);
        parcel.writeString(yUrl);
        parcel.writeString(yID);
    }

    public static final Parcelable.Creator<Music> CREATOR = new Parcelable.Creator<Music>() {
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        public Music[] newArray(int size) {
            return new Music[size];

        }
    };

}


