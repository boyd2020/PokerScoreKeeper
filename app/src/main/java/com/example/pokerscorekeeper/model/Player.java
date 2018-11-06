package com.example.pokerscorekeeper.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable{

    private int id, startingScore, totalScore;
    private String name, email, phone;

    public Player(){}

    public Player(int id, String name, int score, String phone, String email)
    {
        this.id = id;
        this.name = name;
        this.startingScore = score;
        this.phone = phone;
        this.email = email;
    }

    public Player(String name, int score, String phone, String email)
    {
        this.name = name;
        this.startingScore = score;
        this.phone = phone;
        this.email = email;
    }


    protected Player(Parcel in) {
        id = in.readInt();
        startingScore = in.readInt();
        totalScore = in.readInt();
        name = in.readString();
        email = in.readString();
        phone = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(startingScore);
        dest.writeInt(totalScore);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phone);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStartingScore() {
        return startingScore;
    }

    public void setStartingScore(int startingScore) {
        this.startingScore = startingScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
}
