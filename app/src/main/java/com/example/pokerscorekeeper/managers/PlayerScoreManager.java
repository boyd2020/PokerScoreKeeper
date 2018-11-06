package com.example.pokerscorekeeper.managers;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.pokerscorekeeper.widget.StandingWidget;
import com.example.pokerscorekeeper.controllers.ScoreController;
import com.example.pokerscorekeeper.model.PlayerScore;
import com.example.pokerscorekeeper.model.PlayerStanding;
import com.example.pokerscorekeeper.model.ScoreDate;
import com.example.pokerscorekeeper.providers.PokerProvider;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class PlayerScoreManager implements ScoreController {

    private Context context;
    private ContentResolver resolver;

    public PlayerScoreManager(Context context)
    {
        this.context = context;
        this.resolver = context.getContentResolver();
    }

    @Override
    public int addScore(PlayerScore s) {
        ContentValues values = new ContentValues();
        values.put(Constants.PLAYER_ID, s.getPlayerId());
        values.put(Constants.SCORE_SHOWING_UP, s.getShowingUp());
        values.put(Constants.SCORE_PLACING, s.getPlacing());
        values.put(Constants.SCORE_HIGH_HAND, s.getHighHand());
        values.put(Constants.SCORE_FINAL_TABLE, s.getFinalTable());
        values.put(Constants.SCORE_DATE, s.getDate());

        Uri uri = PokerProvider.SCORE_URI;
        String selection = Constants.SCORE_DATE + " =? AND " + Constants.PLAYER_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(s.getDate()), String.valueOf(s.getPlayerId())};

        //Gets the number of rows updated from the insert query
        int update = resolver.update(uri, values, selection, selectionArgs);

        //Insert score if no rows were updated; stores the new id in the update variable
        if(update == 0)
            update = (int) ContentUris.parseId(resolver.insert(uri, values));

        //Updates the widget
        StandingWidget.sendUpdateRequest(context);

        return update;
    }

    @Override
    public int updateScore(PlayerScore s) {
        ContentValues values = new ContentValues();
        values.put(Constants.PLAYER_ID, s.getPlayerId());
        values.put(Constants.SCORE_SHOWING_UP, s.getShowingUp());
        values.put(Constants.SCORE_PLACING, s.getPlacing());
        values.put(Constants.SCORE_HIGH_HAND, s.getHighHand());
        values.put(Constants.SCORE_FINAL_TABLE, s.getFinalTable());
        values.put(Constants.SCORE_DATE, s.getDate());

        String selection = Constants.SCORE_DATE + " =? AND " + Constants.PLAYER_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(s.getDate()), String.valueOf(s.getPlayerId())};


        Uri uri = PokerProvider.SCORE_URI;
        int update = resolver.update(uri, values, selection, selectionArgs);

        //Update the score if the date if found for the player
        if(update == 0) {
            return (int) ContentUris.parseId(resolver.insert(uri, values));
        }
        //Updates the score normally
        else
        {
            uri = ContentUris.withAppendedId(PokerProvider.SCORE_URI, s.getId());
            update = resolver.update(uri, values, null, null);
        }

        //Updates the widget
        StandingWidget.sendUpdateRequest(context);

        return update;
    }

    @Override
    public int deleteScore(int scoreId) {
        Uri uri = ContentUris.withAppendedId(PokerProvider.SCORE_URI, scoreId);
        int rows = resolver.delete(uri, null, null);

        //Updates the widget
        StandingWidget.sendUpdateRequest(context);

        return rows;
    }

    @Override
    public Loader<Cursor> getScoreDates() {
        String[] projection = new String[]{Constants.SCORE_ID, Constants.SCORE_DATE};


        String selection = Constants.SCORES_TABLE + "." + Constants.PLAYER_ID + " = " + Constants.PLAYER_TABLE + "."
                + Constants.PLAYER_ID;

        String sortOrder = Constants.SCORE_DATE + " DESC";

        Uri uri = PokerProvider.SCORE_URI;
        return new CursorLoader(context, uri, projection, selection, null, sortOrder);
    }

    @Override
    public ArrayList<ScoreDate> getScoreDatesFromCursor(Cursor cursor) {
        LinkedHashSet<ScoreDate> dates = new LinkedHashSet<>();
        PlayerManager manager = new PlayerManager(context);

        if(cursor.moveToFirst()) {
            do {
                ScoreDate d = new ScoreDate();
                d.setPlayers(manager.getPlayerCountByDate(cursor.getString(cursor.getColumnIndex(Constants.SCORE_DATE))));
                d.setDate(cursor.getString(cursor.getColumnIndex(Constants.SCORE_DATE)));
                dates.add(d);

            } while (cursor.moveToNext());
        }

        return new ArrayList<ScoreDate>(dates);
    }

    @Override
    public Loader<Cursor> getScoresByDate(String date) {
        String[] projection = new String[]{Constants.SCORE_ID, Constants.PLAYER_TABLE + "." + Constants.PLAYER_ID, Constants.PLAYER_NAME, Constants.SCORE_DATE, Constants.SCORE_SHOWING_UP,
        Constants.SCORE_PLACING, Constants.SCORE_HIGH_HAND, Constants.SCORE_FINAL_TABLE};

        String selection = Constants.SCORES_TABLE + "." + Constants.PLAYER_ID + " = " + Constants.PLAYER_TABLE + "."
        + Constants.PLAYER_ID + " AND " + Constants.SCORE_DATE + " =?";

        String[] selectionArgs = new String[]{date};

        Uri uri = PokerProvider.SCORE_URI;
        return new CursorLoader(context, uri, projection, selection, selectionArgs, null);
    }

    @Override
    public ArrayList<PlayerScore> getPlayerScoresFromCursor(Cursor cursor) {
        ArrayList<PlayerScore> scores = new ArrayList<>();

        if(cursor.moveToFirst())
        {
            do{
                PlayerScore s = new PlayerScore();
                s.setPlayerName(cursor.getString(cursor.getColumnIndex(Constants.PLAYER_NAME)));
                s.setId(cursor.getInt(cursor.getColumnIndex(Constants.SCORE_ID)));
                s.setPlayerId(cursor.getInt(cursor.getColumnIndex(Constants.PLAYER_ID)));
                s.setShowingUp(cursor.getInt(cursor.getColumnIndex(Constants.SCORE_SHOWING_UP)));
                s.setPlacing(cursor.getInt(cursor.getColumnIndex(Constants.SCORE_PLACING)));
                s.setHighHand(cursor.getInt(cursor.getColumnIndex(Constants.SCORE_HIGH_HAND)));
                s.setFinalTable(cursor.getInt(cursor.getColumnIndex(Constants.SCORE_FINAL_TABLE)));
                s.setDate(cursor.getString(cursor.getColumnIndex(Constants.SCORE_DATE)));

                scores.add(s);
            } while (cursor.moveToNext());
        }

        return scores;
    }

    @Override
    public Loader<Cursor> getScoresByPlayerId(int playerId) {
        String[] projection = new String[]{Constants.SCORE_ID, Constants.PLAYER_TABLE + "." + Constants.PLAYER_ID, Constants.PLAYER_NAME, Constants.SCORE_DATE, Constants.SCORE_SHOWING_UP,
                Constants.SCORE_PLACING, Constants.SCORE_HIGH_HAND, Constants.SCORE_FINAL_TABLE};

        String selection = Constants.SCORES_TABLE + "." + Constants.PLAYER_ID + " = " + Constants.PLAYER_TABLE + "."
                + Constants.PLAYER_ID + " AND " + Constants.PLAYER_TABLE + "." + Constants.PLAYER_ID + " =?";

        String[] selectionArgs = new String[]{String.valueOf(playerId)};

        String sortOrder = Constants.SCORE_DATE + " DESC";

        Uri uri = PokerProvider.SCORE_URI;
        return new CursorLoader(context, uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public Cursor getPlayerForStandings() {
        SQLiteDatabase db = new DatabaseHandler(context).getReadableDatabase();

        String[] projection = new String[]{Constants.PLAYER_ID, Constants.PLAYER_NAME,
                Constants.PLAYER_PHONE, Constants.PLAYER_EMAIL,
                Constants.PLAYER_STARTING_SCORE};

        Cursor cursor = db.query(Constants.PLAYER_TABLE, projection, null, null, null, null, null);

        return cursor;
    }

    @Override
    public ArrayList<PlayerStanding> getPlayerStandingsForDateFromCursor(Cursor cursor, String fromDate, String toDate) {
        ArrayList<PlayerStanding> standings = new ArrayList<>();

        if(cursor.moveToFirst())
        {
            do {
                PlayerStanding s = new PlayerStanding();
                s.setId(cursor.getInt(cursor.getColumnIndex(Constants.PLAYER_ID)));
                s.setName(cursor.getString(cursor.getColumnIndex(Constants.PLAYER_NAME)));

                int startScore = cursor.getInt(cursor.getColumnIndex(Constants.PLAYER_STARTING_SCORE));
                s.setStandingScore(getTotalStandingsToDate(s.getId(), fromDate, toDate) + startScore);

                standings.add(s);
            } while (cursor.moveToNext());
        }

        return standings;
    }

    @Override
    public ArrayList<PlayerStanding> getPlayerStandingsFromCursor(Cursor cursor) {
        ArrayList<PlayerStanding> standings = new ArrayList<>();

        if(cursor.moveToFirst())
        {
            do {
                PlayerStanding s = new PlayerStanding();
                s.setId(cursor.getInt(cursor.getColumnIndex(Constants.PLAYER_ID)));
                s.setName(cursor.getString(cursor.getColumnIndex(Constants.PLAYER_NAME)));

                int startScore = cursor.getInt(cursor.getColumnIndex(Constants.PLAYER_STARTING_SCORE));
                s.setStandingScore(getTotalStandings(s.getId()) + startScore);

                standings.add(s);
            } while (cursor.moveToNext());
        }

        return standings;
    }

    @Override
    public int getTotalStandingsToDate(int playerId, String fromDate, String toDate) {
        SQLiteDatabase db = new DatabaseHandler(context).getReadableDatabase();

        int total = 0;

        String query = "SELECT SUM( " + Constants.SCORE_SHOWING_UP + ") + SUM( " + Constants.SCORE_PLACING + ")"
        + " + SUM( " + Constants.SCORE_HIGH_HAND + ") + SUM( " + Constants.SCORE_FINAL_TABLE + ") AS " + Constants.TOTAL_SCORE
        + " FROM " + Constants.SCORES_TABLE  + ", " + Constants.PLAYER_TABLE + " WHERE " + Constants.SCORES_TABLE + "."
        + Constants.PLAYER_ID + " = " + Constants.PLAYER_TABLE + "." + Constants.PLAYER_ID + " AND "
        + Constants.PLAYER_TABLE + "." + Constants.PLAYER_ID + " =? AND " + Constants.SCORE_DATE + " BETWEEN ? AND ?";

        String[] selectionArgs = new String[]{ String.valueOf(playerId), fromDate, toDate};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if(cursor.moveToFirst())
            total = cursor.getInt(cursor.getColumnIndex(Constants.TOTAL_SCORE));


        cursor.close();
        db.close();
        return total;
    }

    public int getTotalStandings(int playerId)
    {
        SQLiteDatabase db = new DatabaseHandler(context).getReadableDatabase();

        int total = 0;

        String query = "SELECT SUM( " + Constants.SCORE_SHOWING_UP + ") + SUM( " + Constants.SCORE_PLACING + ")"
                + " + SUM( " + Constants.SCORE_HIGH_HAND + ") + SUM( " + Constants.SCORE_FINAL_TABLE + ") AS " + Constants.TOTAL_SCORE
                + " FROM " + Constants.SCORES_TABLE  + ", " + Constants.PLAYER_TABLE + " WHERE " + Constants.SCORES_TABLE + "."
                + Constants.PLAYER_ID + " = " + Constants.PLAYER_TABLE + "." + Constants.PLAYER_ID + " AND "
                + Constants.PLAYER_TABLE + "." + Constants.PLAYER_ID + " =?";

        String[] selectionArgs = new String[]{ String.valueOf(playerId)};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if(cursor.moveToFirst())
            total = cursor.getInt(cursor.getColumnIndex(Constants.TOTAL_SCORE));


        cursor.close();
        db.close();

        return total;
    }
}
