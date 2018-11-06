package com.example.pokerscorekeeper.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class ScoreDate implements Parcelable {

    private String date;
    private int id, players;

    public ScoreDate(){}

    private ScoreDate(Parcel in)
    {
        date = in.readString();
        players = in.readInt();
        id = in.readInt();
    }

    public static final Creator<ScoreDate> CREATOR = new Creator<ScoreDate>() {
        @Override
        public ScoreDate createFromParcel(Parcel in) {
            return new ScoreDate(in);
        }

        @Override
        public ScoreDate[] newArray(int size) {
            return new ScoreDate[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeInt(players);
        dest.writeInt(id);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScoreDate)) return false;
        ScoreDate date1 = (ScoreDate) o;
        return id == date1.id &&
                players == date1.players &&
                Objects.equals(date, date1.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, id, players);
    }
}
