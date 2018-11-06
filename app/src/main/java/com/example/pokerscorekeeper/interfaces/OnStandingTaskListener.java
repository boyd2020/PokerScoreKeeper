package com.example.pokerscorekeeper.interfaces;

import android.database.Cursor;

import com.example.pokerscorekeeper.model.PlayerStanding;

import java.util.ArrayList;

public interface OnStandingTaskListener {

    void onStandingsLoaded(ArrayList<PlayerStanding> standings);
}
