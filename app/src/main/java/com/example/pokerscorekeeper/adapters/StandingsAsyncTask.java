package com.example.pokerscorekeeper.adapters;

import android.database.Cursor;
import android.os.AsyncTask;

import com.example.pokerscorekeeper.interfaces.OnStandingTaskListener;
import com.example.pokerscorekeeper.managers.PlayerScoreManager;
import com.example.pokerscorekeeper.model.PlayerStanding;

import java.util.ArrayList;

public class StandingsAsyncTask extends AsyncTask<String, Void, ArrayList<PlayerStanding>> {

    private Cursor cursor;
    private PlayerScoreManager manager;
    private OnStandingTaskListener callback;

    public StandingsAsyncTask(Cursor cursor, PlayerScoreManager manager, OnStandingTaskListener callback) {
        this.cursor = cursor;
        this.manager = manager;
        this.callback = callback;
    }

    @Override
    protected ArrayList<PlayerStanding> doInBackground(String... strings) {
        //Get standings from the cursor
        return manager.getPlayerStandingsForDateFromCursor(cursor, strings[0], strings[1]);
    }

    @Override
    protected void onPostExecute(ArrayList<PlayerStanding> standings) {
        callback.onStandingsLoaded(standings);
    }
}
