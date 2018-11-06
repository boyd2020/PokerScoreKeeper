package com.example.pokerscorekeeper.activities;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.Window;

import com.example.pokerscorekeeper.R;
import com.example.pokerscorekeeper.playertab.PlayerOverviewFragment;
import com.example.pokerscorekeeper.scorestab.ScoreOverviewFragment;
import com.example.pokerscorekeeper.standingstab.StandingsOverviewFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String BUNDLE_NAV_ID = "navID";

    @BindView(R.id.bottomNavigationView)
    BottomNavigationView navigationView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.adView)
    AdView mAdView;


    private int navId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Fade());
        getWindow().setEnterTransition(new Explode());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //deleteDatabase(DatabaseHandler.DATABASE_NAME);

        if(savedInstanceState != null)
            navId = savedInstanceState.getInt(BUNDLE_NAV_ID);
        else
            navId = R.id.tabPlayers;


        setSupportActionBar(toolbar);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(navId);

        MobileAds.initialize(this, "ca-app-pub-1346078373160827~8589576000");

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_NAV_ID, navId);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment;

        switch (item.getItemId())
        {
            case R.id.tabPlayers:
                navId = R.id.tabPlayers;
                fragment = new PlayerOverviewFragment();
                toolbar.setTitle(getString(R.string.menu_players));
                break;

            case R.id.tabScores:
                navId = R.id.tabScores;
                fragment = new ScoreOverviewFragment();
                toolbar.setTitle(getString(R.string.menu_scores));
                break;

            case R.id.tabStandings:
                navId = R.id.tabStandings;
                fragment = new StandingsOverviewFragment();
                toolbar.setTitle(getString(R.string.menu_standings));
                break;

            default:
                fragment = new StandingsOverviewFragment();
                toolbar.setTitle(getString(R.string.menu_standings));
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
        return true;
    }
}
