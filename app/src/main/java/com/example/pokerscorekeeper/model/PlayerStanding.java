package com.example.pokerscorekeeper.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PlayerStanding implements Parcelable {

    private int id, standingScore;
    private String name;


    public PlayerStanding(){}

    private PlayerStanding(Parcel in)
    {
        id = in.readInt();
        standingScore = in.readInt();
        name = in.readString();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(standingScore);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlayerStanding> CREATOR = new Creator<PlayerStanding>() {
        @Override
        public PlayerStanding createFromParcel(Parcel in) {
            return new PlayerStanding(in);
        }

        @Override
        public PlayerStanding[] newArray(int size) {
            return new PlayerStanding[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStandingScore() {
        return standingScore;
    }

    public void setStandingScore(int standingScore) {
        this.standingScore = standingScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
