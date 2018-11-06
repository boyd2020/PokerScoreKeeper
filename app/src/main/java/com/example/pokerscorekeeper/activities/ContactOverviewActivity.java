package com.example.pokerscorekeeper.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.view.Window;

import com.example.pokerscorekeeper.R;
import com.example.pokerscorekeeper.model.Player;
import com.example.pokerscorekeeper.playertab.ContactOverviewFragment;
import com.example.pokerscorekeeper.utils.BundleUtils;

public class ContactOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Fade());
        getWindow().setEnterTransition(new Explode());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);

        if(savedInstanceState == null) {
            Player player = getIntent().getParcelableExtra(BundleUtils.EXTRA_PLAYER);

            Bundle bundle = new Bundle();
            bundle.putInt(BundleUtils.EXTRA_PLAYER_ID, player.getId());

            Fragment fragment = new ContactOverviewFragment();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
        }

    }

}
