package com.example.pokerscorekeeper.standingstab;

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
import com.example.pokerscorekeeper.activities.EditPlayerActivity;
import com.example.pokerscorekeeper.activities.EditScoreActivity;
import com.example.pokerscorekeeper.R;
import com.example.pokerscorekeeper.adapters.ScoreDetailAdapter;
import com.example.pokerscorekeeper.interfaces.OnCardClickedListener;
import com.example.pokerscorekeeper.managers.PlayerManager;
import com.example.pokerscorekeeper.managers.PlayerScoreManager;
import com.example.pokerscorekeeper.model.PlayerScore;
import com.example.pokerscorekeeper.model.PlayerStanding;
import com.example.pokerscorekeeper.utils.BundleUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StandingsDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        OnCardClickedListener {

    private static final int LOADER_ID = 25;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    //Objects/Variables
    private PlayerStanding standing;
    private Unbinder unbinder;
    private PlayerScoreManager manager;
    private ScoreDetailAdapter adapter;
    private String startDate, endDate;

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
            standing = savedInstanceState.getParcelable(BundleUtils.EXTRA_PLAYER_STANDING);
        else
            standing = getArguments().getParcelable(BundleUtils.EXTRA_PLAYER_STANDING);


        manager = new PlayerScoreManager(getContext());
        adapter = new ScoreDetailAdapter(getContext(), new ArrayList<PlayerScore>(), this);

        recyclerView.setAdapter(adapter);

        getActivity().setTitle(getString(R.string.title_score,standing.getName()));
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contact_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId())
        {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                break;

            case R.id.menuEditContact:
                intent = new Intent(getContext(), EditPlayerActivity.class);
                intent.putExtra(BundleUtils.EXTRA_PLAYER, new PlayerManager(getContext()).getPlayerFromDB(standing.getId()));
                startActivity(intent);
                break;

            case R.id.menuAddScore:
                intent = new Intent(getContext(), AddScoreActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return manager.getScoresByPlayerId(standing.getId());
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
