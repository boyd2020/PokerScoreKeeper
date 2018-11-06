package com.example.pokerscorekeeper.controllers;

import android.database.Cursor;
import android.support.v4.content.Loader;

import com.example.pokerscorekeeper.model.Player;

import java.util.ArrayList;

public interface PlayerController {

    int addPlayer(Player player);
    int updatePlayer(Player player);
    int deletePlayer(int playerId);
    int getPlayerCountByDate(String date);
    Loader<Cursor> getPlayerInfo(int playerId);
    Loader<Cursor> getAllPlayers();
    ArrayList<Player> getPlayersFromCursor(Cursor cursor);
    Player getPlayerFromCursor(Cursor cursor);
    Player getPlayerFromDB(int id);

}
