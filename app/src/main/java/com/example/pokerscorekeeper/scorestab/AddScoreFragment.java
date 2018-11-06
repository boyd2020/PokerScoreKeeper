package com.example.pokerscorekeeper.scorestab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pokerscorekeeper.R;
import com.example.pokerscorekeeper.dialogs.DatePickerDialog;
import com.example.pokerscorekeeper.dialogs.SelectPlayerDialog;
import com.example.pokerscorekeeper.managers.PlayerScoreManager;
import com.example.pokerscorekeeper.model.Player;
import com.example.pokerscorekeeper.model.PlayerScore;
import com.example.pokerscorekeeper.services.PokerIntentService;
import com.example.pokerscorekeeper.utils.BundleUtils;
import com.example.pokerscorekeeper.utils.DateUtils;
import com.example.pokerscorekeeper.widget.StandingWidget;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AddScoreFragment extends Fragment implements DatePickerDialog.OnDatePickListener,
        SelectPlayerDialog.OnPlayerSelectedListener, View.OnClickListener {

    @BindView(R.id.playerName)
    TextView playerName;

    @BindView(R.id.scoreDate)
    TextView scoreDate;

    @BindView(R.id.showingUp)
    EditText showingUpScore;

    @BindView(R.id.scorePlacing)
    EditText placingScore;

    @BindView(R.id.scoreHighHand)
    EditText highHandScore;

    @BindView(R.id.scoreFinalTable)
    EditText finalTableScore;

    @BindView(R.id.scoreFragment)
    LinearLayout scoreFragment;

    //Object/Variables
    private int playerId;
    private Unbinder unbinder;
    private Calendar calendar;
    private SimpleDateFormat format;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_score_fragment, container,false);
        unbinder = ButterKnife.bind(this, v);

        format = new SimpleDateFormat(DateUtils.DATE_FORMAT_DEFAULT);

        if(savedInstanceState != null)
            calendar = (Calendar) savedInstanceState.getSerializable(BundleUtils.EXTRA_PLAYER_SCORE_DATE);
        else
            calendar = new GregorianCalendar();

        scoreDate.setText(format.format(calendar.getTimeInMillis()));

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BundleUtils.EXTRA_PLAYER_SCORE_DATE, calendar);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.playerName, R.id.scoreDate, R.id.fab, R.id.clear})
    @Override
    public void onClick(View v) {
        DialogFragment fragment;

        switch (v.getId())
        {
            case R.id.playerName:
                fragment = new SelectPlayerDialog();
                fragment.setTargetFragment(this, 0);
                fragment.show(getActivity().getSupportFragmentManager(), "PLAYER_NAME");
                break;

            case R.id.scoreDate:
                fragment = new DatePickerDialog();
                fragment.setTargetFragment(this, 1);
                fragment.show(getActivity().getSupportFragmentManager(), "SCORE_DATE");
                break;

            case R.id.fab:
                addScore();
                break;

            case R.id.clear:
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onDatePick(int day, int month, int year) {
        calendar.set(year, month, day);
        scoreDate.setText(format.format(calendar.getTimeInMillis()));
    }

    @Override
    public void onPlayerSelected(Player player) {
        this.playerId = player.getId();
        playerName.setText(player.getName());
    }

    private void addScore()
    {
        if(playerName.getText().toString().isEmpty())
            Snackbar.make(scoreFragment, getString(R.string.error_player_name), Snackbar.LENGTH_LONG).show();

        else if (scoreDate.getText().toString().isEmpty())
            Snackbar.make(scoreFragment, getString(R.string.error_score_date), Snackbar.LENGTH_LONG).show();

        else
        {
            int showingUp = !showingUpScore.getText().toString().isEmpty() ? Integer.parseInt(showingUpScore.getText().toString()) : 0;
            int placing = !placingScore.getText().toString().isEmpty() ? Integer.parseInt(placingScore.getText().toString()) : 0;
            int highHand = !highHandScore.getText().toString().isEmpty() ? Integer.parseInt(highHandScore.getText().toString()) : 0;
            int finalTable = !finalTableScore.getText().toString().isEmpty() ? Integer.parseInt(finalTableScore.getText().toString()) : 0;
            String date = new SimpleDateFormat(DateUtils.DATE_FORMAT_SQL).format(calendar.getTimeInMillis());

            //Add the player score
            PlayerScore score = new PlayerScore(playerId, date, showingUp, placing, highHand, finalTable);
            new PlayerScoreManager(getContext()).addScore(score);


            //Update the widget
            StandingWidget.sendUpdateRequest(getContext());
            getActivity().finish();
        }
    }
}
