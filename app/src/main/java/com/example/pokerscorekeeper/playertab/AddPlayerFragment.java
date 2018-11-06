package com.example.pokerscorekeeper.playertab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.pokerscorekeeper.R;
import com.example.pokerscorekeeper.model.Player;
import com.example.pokerscorekeeper.services.PokerIntentService;
import com.example.pokerscorekeeper.utils.BundleUtils;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AddPlayerFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.playerName)
    EditText playerName;

    @BindView(R.id.playerEmail)
    EditText playerEmail;

    @BindView(R.id.playerPhone)
    EditText playerPhone;

    @BindView(R.id.playerStartingScore)
    EditText startingScore;

    @BindView(R.id.playerFragment)
    LinearLayout playerFragment;

    private Unbinder unbinder;
    private FirebaseAnalytics mFirebaseAnalytics;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_player_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());


        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fab, R.id.clear})
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fab:
                addPlayer();
                break;

            case R.id.clear:
                getActivity().finish();
                break;
        }
    }

    private void addPlayer()
    {
        if(!playerName.getText().toString().isEmpty())
        {
            String name = playerName.getText().toString();

            //Gets the starting score
            int score = !startingScore.getText().toString().isEmpty() ? Integer.parseInt(startingScore.getText().toString()) : 0;


            String email = !playerEmail.getText().toString().isEmpty() ? playerEmail.getText().toString() : getString(R.string.label_empty);
            String phone = !playerPhone.getText().toString().isEmpty() ? playerPhone.getText().toString() : getString(R.string.label_empty);

            Player player = new Player(name, score, phone, email);

            //Create an intent and save the new player through a service
            Intent intent = new Intent(getActivity(), PokerIntentService.class);
            intent.setAction(PokerIntentService.ACTION_ADD_PLAYER);
            intent.putExtra(BundleUtils.EXTRA_PLAYER, player);
            getActivity().startService(intent);


            //Create Bundle for FireBase event
            //These events will be used to help validate the need to add an online database in the future
            Bundle params = new Bundle();
            params.putString(BundleUtils.EXTRA_PLAYER_NAME, name);
            params.putString(BundleUtils.EXTRA_PLAYER_EMAIL, email);
            mFirebaseAnalytics.logEvent(BundleUtils.EXTRA_PLAYER, params);

            getActivity().finish();
        }
        else
            Snackbar.make(playerFragment, getString(R.string.error_player_name), Snackbar.LENGTH_LONG).show();

    }
}
