package com.example.pokerscorekeeper.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.view.Window;

import com.example.pokerscorekeeper.R;
import com.example.pokerscorekeeper.model.Player;
import com.example.pokerscorekeeper.playertab.EditPlayerFragment;
import com.example.pokerscorekeeper.utils.BundleUtils;

public class EditPlayerActivity extends AppCompatActivity {

    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Fade());
        getWindow().setEnterTransition(new Explode());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);

        if(savedInstanceState != null)
            player = savedInstanceState.getParcelable(BundleUtils.EXTRA_PLAYER);
        else
            player = getIntent().getParcelableExtra(BundleUtils.EXTRA_PLAYER);

        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleUtils.EXTRA_PLAYER, player);

        Fragment fragment = new EditPlayerFragment();
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BundleUtils.EXTRA_PLAYER, player);
    }
}
