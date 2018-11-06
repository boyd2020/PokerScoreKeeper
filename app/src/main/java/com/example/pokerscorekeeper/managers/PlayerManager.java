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
import android.util.Log;

import com.example.pokerscorekeeper.controllers.PlayerController;
import com.example.pokerscorekeeper.model.Player;
import com.example.pokerscorekeeper.providers.PokerProvider;
import com.example.pokerscorekeeper.utils.PokerContract;

import java.util.ArrayList;

public class PlayerManager implements PlayerController {

    private Context context;
    private ContentResolver resolver;

    public PlayerManager(Context context)
    {
        this.context = context;
        this.resolver = context.getContentResolver();
    }


    @Override
    public int addPlayer(Player p) {

        ContentValues values = new ContentValues();
        values.put(Constants.PLAYER_NAME, p.getName());
        values.put(Constants.PLAYER_EMAIL, p.getEmail());
        values.put(Constants.PLAYER_PHONE, p.getPhone());
        values.put(Constants.PLAYER_STARTING_SCORE, p.getStartingScore());

        Uri uri = PokerProvider.PLAYER_URI;
        return (int) ContentUris.parseId(resolver.insert(uri, values));
    }

    @Override
    public int updatePlayer(Player p) {

        ContentValues values = new ContentValues();
        values.put(Constants.PLAYER_NAME, p.getName());
        values.put(Constants.PLAYER_EMAIL, p.getEmail());
        values.put(Constants.PLAYER_PHONE, p.getPhone());
        values.put(Constants.PLAYER_STARTING_SCORE, p.getStartingScore());

        String selection = Constants.PLAYER_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(p.getId())};

        Uri uri = PokerProvider.PLAYER_URI;
        return resolver.update(uri, values, selection, selectionArgs);
    }

    @Override
    public int deletePlayer(int playerId) {
        Uri uri = ContentUris.withAppendedId(PokerProvider.PLAYER_URI, playerId);
        return resolver.delete(uri, null, null);
    }

    @Override
    public Loader<Cursor> getAllPlayers() {
        String[] projection = new String[]{Constants.PLAYER_ID, Constants.PLAYER_NAME,
                Constants.PLAYER_PHONE, Constants.PLAYER_EMAIL,
                Constants.PLAYER_STARTING_SCORE};

        Uri uri = PokerProvider.PLAYER_URI;
        return new CursorLoader(context, uri, projection, null, null, null);
    }

    @Override
    public ArrayList<Player> getPlayersFromCursor(Cursor cursor) {
        ArrayList<Player> players = new ArrayList<>();

        PlayerScoreManager manager = new PlayerScoreManager(context);

        if(cursor.moveToFirst())
        {
            do {
                Player p = new Player();
                p.setId(cursor.getInt(cursor.getColumnIndex(Constants.PLAYER_ID)));
                p.setName(cursor.getString(cursor.getColumnIndex(Constants.PLAYER_NAME)));
                p.setEmail(cursor.getString(cursor.getColumnIndex(Constants.PLAYER_EMAIL)));
                p.setPhone(cursor.getString(cursor.getColumnIndex(Constants.PLAYER_PHONE)));
                p.setStartingScore(cursor.getInt(cursor.getColumnIndex(Constants.PLAYER_STARTING_SCORE)));
                p.setTotalScore(manager.getTotalStandings(p.getId()) + p.getStartingScore());
                players.add(p);
            } while (cursor.moveToNext());
        }

        return players;
    }

    @Override
    public int getPlayerCountByDate(String date) {
        SQLiteDatabase db = new DatabaseHandler(context).getReadableDatabase();

        int total = 0;

        String query = "SELECT " + Constants.PLAYER_ID + " FROM "
        + Constants.SCORES_TABLE + " WHERE " + Constants.SCORE_DATE + " =?";

        Cursor cursor = db.rawQuery(query, new String[]{date});

        if(cursor.moveToFirst())
            total = cursor.getCount();

        cursor.close();
        db.close();

        return total;
    }

    @Override
    public Loader<Cursor> getPlayerInfo(int playerId) {
        String[] projection = new String[]{Constants.PLAYER_ID, Constants.PLAYER_NAME, Constants.PLAYER_EMAIL,
        Constants.PLAYER_PHONE, Constants.PLAYER_STARTING_SCORE};

        String selection = Constants.PLAYER_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(playerId)};

        Uri uri = PokerProvider.PLAYER_URI;
        return new CursorLoader(context, uri, projection, selection, selectionArgs, null);
    }

    @Override
    public Player getPlayerFromCursor(Cursor cursor) {
        if(cursor == null)
            return null;

        Player player = new Player();

        if(cursor.moveToFirst())
        {
            player.setId(cursor.getInt(cursor.getColumnIndex(Constants.PLAYER_ID)));
            player.setName(cursor.getString(cursor.getColumnIndex(Constants.PLAYER_NAME)));
            player.setPhone(cursor.getString(cursor.getColumnIndex(Constants.PLAYER_PHONE)));
            player.setEmail(cursor.getString(cursor.getColumnIndex(Constants.PLAYER_EMAIL)));
        }

        return player;
    }

    @Override
    public Player getPlayerFromDB(int playerId) {
        SQLiteDatabase db = new DatabaseHandler(context).getReadableDatabase();

        String[] projection = new String[]{Constants.PLAYER_ID, Constants.PLAYER_NAME, Constants.PLAYER_EMAIL,
                Constants.PLAYER_PHONE, Constants.PLAYER_STARTING_SCORE};

        String selection = Constants.PLAYER_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(playerId)};

        Cursor cursor = db.query(Constants.PLAYER_TABLE, projection, selection, selectionArgs, null, null, null);

        Player player = getPlayerFromCursor(cursor);

        cursor.close();
        db.close();

        return player;
    }
}
