package com.example.pokerscorekeeper.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PlayerScore implements Parcelable {

    private String date, playerName;
    private int id, playerId, showingUp, placing, highHand, finalTable;

    public PlayerScore(){}

    public PlayerScore(int id, int playerId, String date, int showingUp, int placing, int highHand, int finalTable)
    {
        this.id = id;
        this.playerId = playerId;
        this.date = date;
        this.showingUp = showingUp;
        this.placing = placing;
        this.highHand = highHand;
        this.finalTable = finalTable;
    }

    public PlayerScore(int playerId, String date, int showingUp, int placing, int highHand, int finalTable)
    {
        this.playerId = playerId;
        this.date = date;
        this.showingUp = showingUp;
        this.placing = placing;
        this.highHand = highHand;
        this.finalTable = finalTable;
    }


    protected PlayerScore(Parcel in) {
        date = in.readString();
        playerName = in.readString();
        id = in.readInt();
        playerId = in.readInt();
        showingUp = in.readInt();
        placing = in.readInt();
        highHand = in.readInt();
        finalTable = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(playerName);
        dest.writeInt(id);
        dest.writeInt(playerId);
        dest.writeInt(showingUp);
        dest.writeInt(placing);
        dest.writeInt(highHand);
        dest.writeInt(finalTable);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlayerScore> CREATOR = new Creator<PlayerScore>() {
        @Override
        public PlayerScore createFromParcel(Parcel in) {
            return new PlayerScore(in);
        }

        @Override
        public PlayerScore[] newArray(int size) {
            return new PlayerScore[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getShowingUp() {
        return showingUp;
    }

    public void setShowingUp(int showingUp) {
        this.showingUp = showingUp;
    }

    public int getPlacing() {
        return placing;
    }

    public void setPlacing(int placing) {
        this.placing = placing;
    }

    public int getHighHand() {
        return highHand;
    }

    public void setHighHand(int highHand) {
        this.highHand = highHand;
    }

    public int getFinalTable() {
        return finalTable;
    }

    public void setFinalTable(int finalTable) {
        this.finalTable = finalTable;
    }

    public int getTotalScore() {
        return showingUp + placing + highHand + finalTable;
    }
}
