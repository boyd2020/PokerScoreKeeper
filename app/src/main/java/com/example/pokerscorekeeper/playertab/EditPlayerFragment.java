package com.example.pokerscorekeeper.playertab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pokerscorekeeper.R;
import com.example.pokerscorekeeper.model.Player;
import com.example.pokerscorekeeper.services.PokerIntentService;
import com.example.pokerscorekeeper.utils.BundleUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EditPlayerFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.playerName)
    EditText playerName;

    @BindView(R.id.playerEmail)
    EditText playerEmail;

    @BindView(R.id.playerPhone)
    EditText playerPhone;

    @BindView(R.id.playerStartingScore)
    EditText startingScore;

    @BindView(R.id.playerTitle)
    TextView playerTitle;

    @BindView(R.id.playerFragment)
    LinearLayout playerFragment;

    private Unbinder unbinder;
    private Player player;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_player_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);


        if(savedInstanceState != null)
            player = savedInstanceState.getParcelable(BundleUtils.EXTRA_PLAYER);
        else
            player = getArguments().getParcelable(BundleUtils.EXTRA_PLAYER);

        playerName.setText(player.getName());
        playerEmail.setText(player.getEmail());
        playerPhone.setText(player.getPhone());
        startingScore.setText(String.valueOf(player.getStartingScore()));
        playerTitle.setText(getString(R.string.label_edit_player));

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BundleUtils.EXTRA_PLAYER, player);
    }

    @OnClick({R.id.fab, R.id.clear})
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fab:
                updatePlayer();
                break;

            case R.id.clear:
                getActivity().finish();
                break;
        }
    }

    private void updatePlayer()
    {
        if(!playerName.getText().toString().isEmpty())
        {
            String name = playerName.getText().toString();

            //Gets the starting score
            int score = !startingScore.getText().toString().isEmpty() ? Integer.parseInt(startingScore.getText().toString()) : 0;


            int id = player.getId();
            String email = !playerEmail.getText().toString().isEmpty() ? playerEmail.getText().toString() : getString(R.string.label_empty);
            String phone = !playerPhone.getText().toString().isEmpty() ? playerPhone.getText().toString() : getString(R.string.label_empty);

            player = new Player(id,name, score, phone, email);

            Intent intent = new Intent(getActivity(), PokerIntentService.class);
            intent.setAction(PokerIntentService.ACTION_UPDATE_PLAYER);
            intent.putExtra(BundleUtils.EXTRA_PLAYER, player);
            getActivity().startService(intent);
            getActivity().finish();
        }
        else
            Snackbar.make(playerFragment, getString(R.string.error_player_name), Snackbar.LENGTH_LONG).show();
    }
}
