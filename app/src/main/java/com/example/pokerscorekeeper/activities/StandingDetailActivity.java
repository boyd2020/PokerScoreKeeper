package com.example.pokerscorekeeper.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.view.Window;

import com.example.pokerscorekeeper.R;
import com.example.pokerscorekeeper.model.PlayerStanding;
import com.example.pokerscorekeeper.standingstab.StandingsDetailFragment;
import com.example.pokerscorekeeper.utils.BundleUtils;

public class StandingDetailActivity extends AppCompatActivity {

    private PlayerStanding standing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Fade());
        getWindow().setEnterTransition(new Explode());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);

        if(savedInstanceState == null) {
            standing = getIntent().getExtras().getParcelable(BundleUtils.EXTRA_PLAYER_STANDING);

            Bundle bundle = new Bundle();
            bundle.putParcelable(BundleUtils.EXTRA_PLAYER_STANDING, standing);

            Fragment fragment = new StandingsDetailFragment();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();

        }
    }

}
