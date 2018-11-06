package com.example.pokerscorekeeper.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.pokerscorekeeper.managers.PlayerManager;
import com.example.pokerscorekeeper.managers.PlayerScoreManager;
import com.example.pokerscorekeeper.model.Player;
import com.example.pokerscorekeeper.model.PlayerScore;
import com.example.pokerscorekeeper.utils.BundleUtils;
import com.example.pokerscorekeeper.widget.StandingWidget;

public class PokerIntentService extends IntentService {

    //Intent Actions for objects
    public static final String ACTION_ADD_PLAYER = "ADD_PLAYER";
    public static final String ACTION_ADD_SCORE = "ADD_SCORE";
    public static final String ACTION_UPDATE_PLAYER = "UPDATE_PLAYER";

    public PokerIntentService() {
        super("PokerIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        Player player;
        PlayerScore score;

        switch (action)
        {
            case ACTION_ADD_PLAYER:
                player = intent.getParcelableExtra(BundleUtils.EXTRA_PLAYER);
                new PlayerManager(getApplicationContext()).addPlayer(player);
                break;

            case ACTION_ADD_SCORE:
                score = intent.getParcelableExtra(BundleUtils.EXTRA_SCORE);
                new PlayerScoreManager(getApplicationContext()).addScore(score);
                break;

            case ACTION_UPDATE_PLAYER:
                player = intent.getParcelableExtra(BundleUtils.EXTRA_PLAYER);
                new PlayerManager(getApplicationContext()).updatePlayer(player);
                break;
        }

        //Update the widget
        StandingWidget.sendUpdateRequest(getApplicationContext());
    }
}
