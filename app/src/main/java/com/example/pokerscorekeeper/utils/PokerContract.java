package com.example.pokerscorekeeper.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class PokerContract {

    public static final String CONTENT_AUTHORITY = "com.example.pokerscorekeeper";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final class PlayerEntry implements BaseColumns {
        //Table Name
        public static final String TABLE_PLAYERS = "players";

        //Columns
        public static final String ID_PLAYER = "playerID";
        public static final String COLUMN_PLAYER_NAME = "playerName";
        public static final String COLUMN_PLAYER_EMAIL = "playerEmail";
        public static final String COLUMN_PLAYER_PHONE = "playerPhone";
        public static final String COLUMN_PLAYER_STARTING_SCORE = "playerStartScore";

        //Content Uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_PLAYERS).build();

        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_PLAYERS;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_PLAYERS;

    }

    public static final class PlayerScoreEntry implements BaseColumns {
        //Table Name
        public static final String TABLE_PLAYER_SCORES = "playerScores";

        //Columns
        public static final String ID_SCORE = "scoreID";
        public static final String COLUMN_SCORE_DATE = "scoreDate";
        public static final String COLUMN_SCORE_SHOWING_UP = "scoreShowingUp";
        public static final String COLUMN_SCORE_PLACING = "scorePlacing";
        public static final String COLUMN_SCORE_HIGH_HAND = "scoreHighHand";
        public static final String COLUMN_SCORE_FINAL_TABLE = "scoreFinalTable";

        //Misc Column Names
        public static final String COLUMN_SCORE_STARTING = "scoreStarting";
        public static final String COLUMN_TOTAL_SCORE = "totalScore";


        //Content Uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_PLAYER_SCORES).build();


        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_PLAYER_SCORES;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_PLAYER_SCORES;
    }
}
