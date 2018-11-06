package com.example.pokerscorekeeper.managers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pokerscorekeeper.utils.PokerContract;

public class DatabaseHandler extends SQLiteOpenHelper {

    //Database name and version
    public static final String DATABASE_NAME = "PokerScoreKeeper.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.CREATE_PLAYER_TABLE);
        db.execSQL(Constants.CREATE_SCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        // Drop the table
        db.execSQL("DROP TABLE IF EXISTS " + PokerContract.PlayerEntry.TABLE_PLAYERS);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                PokerContract.PlayerEntry.TABLE_PLAYERS + "'");

        db.execSQL("DROP TABLE IF EXISTS " + PokerContract.PlayerScoreEntry.TABLE_PLAYER_SCORES);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                PokerContract.PlayerScoreEntry.TABLE_PLAYER_SCORES + "'");

        */
        // re-create database
        onCreate(db);
    }
}
