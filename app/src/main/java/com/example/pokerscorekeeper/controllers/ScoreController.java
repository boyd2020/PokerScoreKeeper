package com.example.pokerscorekeeper.controllers;

import android.database.Cursor;
import android.support.v4.content.Loader;

import com.example.pokerscorekeeper.model.PlayerScore;
import com.example.pokerscorekeeper.model.PlayerStanding;
import com.example.pokerscorekeeper.model.ScoreDate;

import java.util.ArrayList;

public interface ScoreController {

    int addScore(PlayerScore s);
    int updateScore(PlayerScore s);
    int deleteScore(int scoreId);
    int getTotalStandingsToDate(int playerId, String fromDate, String toDate);
    Cursor getPlayerForStandings();
    Loader<Cursor> getScoreDates();
    Loader<Cursor> getScoresByDate(String date);
    Loader<Cursor> getScoresByPlayerId(int playerId);
    ArrayList<ScoreDate> getScoreDatesFromCursor(Cursor cursor);
    ArrayList<PlayerScore> getPlayerScoresFromCursor(Cursor cursor);
    ArrayList<PlayerStanding> getPlayerStandingsFromCursor(Cursor cursor);
    ArrayList<PlayerStanding> getPlayerStandingsForDateFromCursor(Cursor cursor, String fromDate, String toDate);
}
