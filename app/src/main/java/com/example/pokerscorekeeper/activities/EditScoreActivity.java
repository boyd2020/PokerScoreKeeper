package com.example.pokerscorekeeper.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.view.Window;

import com.example.pokerscorekeeper.R;
import com.example.pokerscorekeeper.model.PlayerScore;
import com.example.pokerscorekeeper.scorestab.EditScoreFragment;
import com.example.pokerscorekeeper.utils.BundleUtils;

public class EditScoreActivity extends AppCompatActivity {

    private PlayerScore score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Fade());
        getWindow().setEnterTransition(new Fade());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);

        if(savedInstanceState == null) {
            score = getIntent().getParcelableExtra(BundleUtils.EXTRA_PLAYER_SCORE);

            Bundle bundle = new Bundle();
            bundle.putParcelable(BundleUtils.EXTRA_PLAYER_SCORE, score);

            Fragment fragment = new EditScoreFragment();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BundleUtils.EXTRA_PLAYER_SCORE, score);
    }
}
