package com.example.pokerscorekeeper.scorestab;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.pokerscorekeeper.activities.AddScoreActivity;
import com.example.pokerscorekeeper.activities.EditScoreActivity;
import com.example.pokerscorekeeper.R;
import com.example.pokerscorekeeper.adapters.PlayerScoreCardAdapter;
import com.example.pokerscorekeeper.interfaces.OnCardClickedListener;
import com.example.pokerscorekeeper.managers.PlayerScoreManager;
import com.example.pokerscorekeeper.model.PlayerScore;
import com.example.pokerscorekeeper.utils.BundleUtils;
import com.example.pokerscorekeeper.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ScoreDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        OnCardClickedListener{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //Objects/Variables
    private String date;
    private Unbinder unbinder;
    private PlayerScoreManager manager;
    private PlayerScoreCardAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.collapsing_detail_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null)
            date = savedInstanceState.getString(BundleUtils.EXTRA_DATE);
        else
            date = getArguments().getString(BundleUtils.EXTRA_DATE);


        //Set Activity Title
        try {
            long time = new SimpleDateFormat(DateUtils.DATE_FORMAT_SQL).parse(date).getTime();
            getActivity().setTitle(getString(R.string.title_score, new SimpleDateFormat(DateUtils.DATE_FORMAT_DEFAULT).format(time)));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        manager = new PlayerScoreManager(getContext());
        adapter = new PlayerScoreCardAdapter(getContext(), new ArrayList<PlayerScore>(), this);

        recyclerView.setAdapter(adapter);

        getActivity().getSupportLoaderManager().initLoader(40, null, this);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BundleUtils.EXTRA_DATE, date);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.score_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                break;

            case R.id.menuAddScore:
                Intent intent = new Intent(getContext(), AddScoreActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return manager.getScoresByDate(date);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ArrayList<PlayerScore> scores = manager.getPlayerScoresFromCursor(data);
        adapter.setCursor(scores);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onCardClicked(Object object) {
        PlayerScore score = ((PlayerScore)object);
        Intent intent = new Intent(getContext(), EditScoreActivity.class);
        intent.putExtra(BundleUtils.EXTRA_PLAYER_SCORE, score);
        startActivity(intent);
    }
}
