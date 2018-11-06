package com.example.pokerscorekeeper.standingstab;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pokerscorekeeper.R;
import com.example.pokerscorekeeper.activities.StandingDetailActivity;
import com.example.pokerscorekeeper.adapters.StandingCardAdapter;
import com.example.pokerscorekeeper.adapters.StandingsAsyncTask;
import com.example.pokerscorekeeper.dialogs.DatePickerDialog;
import com.example.pokerscorekeeper.interfaces.OnCardClickedListener;
import com.example.pokerscorekeeper.interfaces.OnStandingTaskListener;
import com.example.pokerscorekeeper.managers.PlayerManager;
import com.example.pokerscorekeeper.managers.PlayerScoreManager;
import com.example.pokerscorekeeper.model.PlayerStanding;
import com.example.pokerscorekeeper.utils.BundleUtils;
import com.example.pokerscorekeeper.utils.DateUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class StandingsOverviewFragment extends Fragment implements DatePickerDialog.OnDatePickListener,
        View.OnClickListener, OnCardClickedListener, OnStandingTaskListener, LoaderManager.LoaderCallbacks<Cursor> {

    //Constants
    private static final int DATE_TYPE_FROM = 0, DATE_TYPE_TO = 1, LOADER_ID = 120;


    @BindView(R.id.fromDateTextView)
    TextView fromDateTextView;

    @BindView(R.id.toDateTextView)
    TextView toDateTextView;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.barChart)
    BarChart barChart;

    @BindView(R.id.emptyText)
    TextView emptyText;


    //Object/Variables
    private int type;
    private Unbinder unbinder;
    private Calendar toCal, fromCal;
    private SimpleDateFormat format;
    private PlayerScoreManager manager;
    private StandingCardAdapter adapter;
    private ArrayList<BarEntry> chartValues;
    private ArrayList<String> chartLabels;
    private BarDataSet barDataSet;
    private BarData barData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.standing_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);

        format = new SimpleDateFormat(DateUtils.DATE_FORMAT_DEFAULT);
        manager = new PlayerScoreManager(getContext());
        adapter = new StandingCardAdapter(getContext(), new ArrayList<PlayerStanding>(), this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        chartValues = new ArrayList<>();
        chartLabels = new ArrayList<>();

        if(savedInstanceState != null) {
            fromCal = (Calendar) savedInstanceState.getSerializable(BundleUtils.EXTRA_PLAYER_START_DATE);
            toCal = (Calendar) savedInstanceState.getSerializable(BundleUtils.EXTRA_PLAYER_END_DATE);
        }
        else
        {
            toCal = new GregorianCalendar();
            fromCal = new GregorianCalendar();
        }

        toDateTextView.setText(format.format(toCal.getTimeInMillis()));
        fromDateTextView.setText(format.format(fromCal.getTimeInMillis()));

        getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BundleUtils.EXTRA_PLAYER_START_DATE, fromCal);
        outState.putSerializable(BundleUtils.EXTRA_PLAYER_END_DATE, toCal);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Retrieves the players
        return new PlayerManager(getContext()).getAllPlayers();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data != null) {

            //Creates the Date Format
            SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_FORMAT_SQL);

            //Pulls the start and end dates
            String fromDate = df.format(fromCal.getTimeInMillis());
            String toDate = df.format(toCal.getTimeInMillis());

            //Async Task to search for player standings by date
            StandingsAsyncTask task = new StandingsAsyncTask(data, manager, this);
            task.execute(new String[]{fromDate, toDate});
        }
        else
            emptyText.setText(getString(R.string.empty_scores));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onDatePick(int day, int month, int year) {
        switch (type)
        {
            case DATE_TYPE_FROM:
                fromCal.set(year, month, day);
                fromDateTextView.setText(format.format(fromCal.getTimeInMillis()));
                break;

            case DATE_TYPE_TO:
                toCal.set(year, month, day);
                toDateTextView.setText(format.format(toCal.getTimeInMillis()));
                break;
        }

    }


    @OnClick({R.id.toDateCard, R.id.fromDateCard})
    @Override
    public void onClick(View v) {
        DialogFragment fragment;

        switch (v.getId())
        {
            case R.id.fromDateCard:
                type = DATE_TYPE_FROM;
                fragment = new DatePickerDialog();
                fragment.setTargetFragment(this, 0);
                fragment.show(getActivity().getSupportFragmentManager(), "FROM_DATE");
                break;

            case R.id.toDateCard:
                type = DATE_TYPE_TO;
                fragment = new DatePickerDialog();
                fragment.setTargetFragment(this, 1);
                fragment.show(getActivity().getSupportFragmentManager(), "TO_DATE");
                break;
        }
    }

    @Override
    public void onCardClicked(Object object) {
        PlayerStanding standing = ((PlayerStanding) object);
        Intent intent = new Intent(getContext(), StandingDetailActivity.class);
        intent.putExtra(BundleUtils.EXTRA_PLAYER_STANDING, standing);
        startActivity(intent);
    }

    //Returns the players along with the standings from the AsyncTask
    @Override
    public void onStandingsLoaded(ArrayList<PlayerStanding> standings) {
        //Update the recyclerView
        adapter.setCursor(standings);

        //Update the standings chart
        updateBarData(standings);
    }

    private void updateBarData(ArrayList<PlayerStanding> standings)
    {
        chartLabels.clear();
        chartValues.clear();

        //Add Chart Labels
        for(int i = 0; i < standings.size(); i++)
        {
            PlayerStanding s = standings.get(i);

            chartLabels.add(s.getName());
            chartValues.add(new BarEntry(s.getStandingScore(), i));
        }

        barDataSet = new BarDataSet(chartValues, getString(R.string.label_chart_title));
        barData = new BarData(chartLabels, barDataSet);
        barChart.setData(barData);
        barChart.setDescription(getString(R.string.label_chart_description));
        barChart.notifyDataSetChanged();
        barChart.invalidate();
        barChart.animateY(getResources().getInteger(R.integer.anim_duration));
    }
}
