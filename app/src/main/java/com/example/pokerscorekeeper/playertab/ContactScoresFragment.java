package com.example.pokerscorekeeper.playertab;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pokerscorekeeper.activities.EditScoreActivity;
import com.example.pokerscorekeeper.R;
import com.example.pokerscorekeeper.adapters.ScoreDetailAdapter;
import com.example.pokerscorekeeper.interfaces.OnCardClickedListener;
import com.example.pokerscorekeeper.managers.PlayerScoreManager;
import com.example.pokerscorekeeper.model.PlayerScore;
import com.example.pokerscorekeeper.utils.BundleUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ContactScoresFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        OnCardClickedListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private int playerId;
    private Unbinder unbinder;
    private PlayerScoreManager manager;
    private ScoreDetailAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contact_score_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);

        if(savedInstanceState != null)
            playerId = savedInstanceState.getInt(BundleUtils.EXTRA_PLAYER_ID);
        else
            playerId = getArguments().getInt(BundleUtils.EXTRA_PLAYER_ID);


        manager = new PlayerScoreManager(getContext());
        adapter = new ScoreDetailAdapter(getContext(), new ArrayList<PlayerScore>(), this);

        recyclerView.setAdapter(adapter);
        getActivity().getSupportLoaderManager().destroyLoader(200);
        getActivity().getSupportLoaderManager().initLoader(200, null, this);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BundleUtils.EXTRA_PLAYER_ID, playerId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return manager.getScoresByPlayerId(playerId);
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
