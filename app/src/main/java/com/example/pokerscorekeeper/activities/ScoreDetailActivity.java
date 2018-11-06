package com.example.pokerscorekeeper.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.view.Window;

import com.example.pokerscorekeeper.R;
import com.example.pokerscorekeeper.scorestab.ScoreDetailFragment;
import com.example.pokerscorekeeper.utils.BundleUtils;

public class ScoreDetailActivity extends AppCompatActivity {

    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Fade());
        getWindow().setEnterTransition(new Explode());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);

        if(savedInstanceState != null)
            date = savedInstanceState.getString(BundleUtils.EXTRA_DATE);
        else
            date = getIntent().getExtras().getString(BundleUtils.EXTRA_DATE);

        Bundle bundle = new Bundle();
        bundle.putString(BundleUtils.EXTRA_DATE, date);

        Fragment fragment = new ScoreDetailFragment();
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BundleUtils.EXTRA_DATE, date);
    }
}
