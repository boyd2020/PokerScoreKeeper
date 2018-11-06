package com.example.pokerscorekeeper.scorestab;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pokerscorekeeper.activities.AddScoreActivity;
import com.example.pokerscorekeeper.R;
import com.example.pokerscorekeeper.activities.ScoreDetailActivity;
import com.example.pokerscorekeeper.adapters.ScoreDateAdapter;
import com.example.pokerscorekeeper.interfaces.OnCardClickedListener;
import com.example.pokerscorekeeper.managers.PlayerScoreManager;
import com.example.pokerscorekeeper.model.ScoreDate;
import com.example.pokerscorekeeper.utils.BundleUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ScoreOverviewFragment extends Fragment implements OnCardClickedListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 10;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.emptyText)
    TextView emptyText;


    //Objects/Variables
    private Unbinder unbinder;
    private ScoreDateAdapter adapter;
    private PlayerScoreManager manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fab_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);

        manager = new PlayerScoreManager(getContext());
        adapter = new ScoreDateAdapter(getContext(), new ArrayList<ScoreDate>(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddScoreActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
        });

        fab.setContentDescription(getString(R.string.desc_add_score));

        getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID);
        unbinder.unbind();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return manager.getScoreDates();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null) {
            ArrayList<ScoreDate> dates = manager.getScoreDatesFromCursor(data);
            adapter.setCursor(dates);
        }
        else
            emptyText.setText(getString(R.string.empty_scores));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onCardClicked(Object object) {
        String date = ((ScoreDate)object).getDate();

        Intent intent = new Intent(getContext(), ScoreDetailActivity.class);
        intent.putExtra(BundleUtils.EXTRA_DATE, date);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
    }
}
