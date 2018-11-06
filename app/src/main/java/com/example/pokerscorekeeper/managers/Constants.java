package com.example.pokerscorekeeper.managers;

public class Constants {

    public static final String PLAYER_TABLE = "players";
    public static final String PLAYER_ID = "playerID";
    public static final String PLAYER_NAME = "playerName";
    public static final String PLAYER_EMAIL = "playerEmail";
    public static final String PLAYER_PHONE = "playerPhone";
    public static final String PLAYER_STARTING_SCORE = "startingScore";


    public static final String SCORES_TABLE = "scores";
    public static final String SCORE_ID = "scoreID";
    public static final String SCORE_DATE = "scoreDate";
    public static final String SCORE_SHOWING_UP = "showingUp";
    public static final String SCORE_PLACING = "placing";
    public static final String SCORE_HIGH_HAND = "HighHand";
    public static final String SCORE_FINAL_TABLE = "FinalTable";

    public static final String TOTAL_SCORE = "totalScore";

    public static final String CREATE_PLAYER_TABLE = "CREATE TABLE " + PLAYER_TABLE
    + "( " + PLAYER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PLAYER_NAME + " TEXT, "
    + PLAYER_EMAIL + " TEXT, " + PLAYER_PHONE + " TEXT, " + PLAYER_STARTING_SCORE + " INTEGER);";

    public static final String CREATE_SCORE_TABLE = "CREATE TABLE " + SCORES_TABLE
    + "( " + SCORE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PLAYER_ID + " INTEGER, "
    + SCORE_DATE + " DATE, " + SCORE_SHOWING_UP + " INTEGER, " + SCORE_PLACING + " INTEGER, "
    + SCORE_HIGH_HAND + " INTEGER, " + SCORE_FINAL_TABLE +  " INTEGER);";
}
