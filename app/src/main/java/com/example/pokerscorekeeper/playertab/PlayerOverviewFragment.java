package com.example.pokerscorekeeper.playertab;

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

import com.example.pokerscorekeeper.activities.AddPlayerActivity;
import com.example.pokerscorekeeper.activities.ContactOverviewActivity;
import com.example.pokerscorekeeper.R;
import com.example.pokerscorekeeper.adapters.PlayerCardAdapter;
import com.example.pokerscorekeeper.interfaces.OnCardClickedListener;
import com.example.pokerscorekeeper.managers.PlayerManager;
import com.example.pokerscorekeeper.model.Player;
import com.example.pokerscorekeeper.utils.BundleUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PlayerOverviewFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor>,
        OnCardClickedListener{


    private static final int LOADER_ID = 0;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.emptyText)
    TextView emptyText;

    //Objects/Variables
    private Unbinder unbinder;
    private PlayerManager playerManager;
    private PlayerCardAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fab_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);

        playerManager = new PlayerManager(getContext());
        adapter = new PlayerCardAdapter(getContext(), new ArrayList<Player>(), this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddPlayerActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
        });

        fab.setContentDescription(getString(R.string.desc_add_player));

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
        return playerManager.getAllPlayers();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null) {
            ArrayList<Player> players = playerManager.getPlayersFromCursor(data);
            adapter.setCursor(players);
        }
        else
            emptyText.setText(getString(R.string.empty_players));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onCardClicked(Object object) {
        Player player = (Player) object;

        Intent intent = new Intent(getContext(), ContactOverviewActivity.class);
        intent.putExtra(BundleUtils.EXTRA_PLAYER, player);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
    }
}
