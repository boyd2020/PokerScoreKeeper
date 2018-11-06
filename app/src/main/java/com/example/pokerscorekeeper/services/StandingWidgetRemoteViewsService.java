package com.example.pokerscorekeeper.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.pokerscorekeeper.widget.StandingsRemoteViewsFactory;

public class StandingWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StandingsRemoteViewsFactory(getApplicationContext());
    }
}
