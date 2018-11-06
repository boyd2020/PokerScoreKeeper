package com.example.pokerscorekeeper.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.pokerscorekeeper.R;
import com.example.pokerscorekeeper.managers.PlayerScoreManager;
import com.example.pokerscorekeeper.model.PlayerStanding;
import com.example.pokerscorekeeper.utils.BundleUtils;

import java.util.ArrayList;

public class StandingsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    //Objects
    private Context context;
    private PlayerScoreManager manager;
    private ArrayList<PlayerStanding> standings;

    public StandingsRemoteViewsFactory(Context context) {
        this.context = context;
        manager = new PlayerScoreManager(context);
        standings = new ArrayList<>();
    }

    @Override
    public void onCreate() {
        getStandings();
    }

    @Override
    public void onDataSetChanged() {
        long identityToken = Binder.clearCallingIdentity();
        getStandings();
        Binder.restoreCallingIdentity(identityToken);
    }

    private void getStandings()
    {
        standings.clear();
        standings.addAll(manager.getPlayerStandingsFromCursor(manager.getPlayerForStandings()));
    }

    @Override
    public void onDestroy() {
        standings.clear();
    }

    @Override
    public int getCount() {
        return standings.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if(standings.isEmpty())
            return null;

        PlayerStanding s = standings.get(position);

        //Create Remote Views Object
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.standing_card_widget);
        view.setTextViewText(R.id.playerName, s.getName());
        view.setTextViewText(R.id.playerScore, String.valueOf(s.getStandingScore()));

        Intent fillIntent = new Intent();
        fillIntent.putExtra(BundleUtils.EXTRA_PLAYER_STANDING, s);
        view.setOnClickFillInIntent(R.id.standingCard, fillIntent);

        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
