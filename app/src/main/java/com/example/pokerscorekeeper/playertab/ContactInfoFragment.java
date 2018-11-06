package com.example.pokerscorekeeper.playertab;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pokerscorekeeper.R;
import com.example.pokerscorekeeper.managers.PlayerManager;
import com.example.pokerscorekeeper.model.Player;
import com.example.pokerscorekeeper.utils.BundleUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ContactInfoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.playerPhone)
    TextView playerPhone;

    @BindView(R.id.playerEmail)
    TextView playerEmail;

    //Objects
    private int playerId;
    private Unbinder unbinder;
    private PlayerManager manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contact_info_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);

        if(savedInstanceState != null)
            playerId = savedInstanceState.getInt(BundleUtils.EXTRA_PLAYER_ID);
        else
            playerId = getArguments().getInt(BundleUtils.EXTRA_PLAYER_ID);

        manager = new PlayerManager(getContext());

        getActivity().getSupportLoaderManager().destroyLoader(77);
        getActivity().getSupportLoaderManager().initLoader(77, null, this);

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
        return manager.getPlayerInfo(playerId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Player player = manager.getPlayerFromCursor(data);

        playerPhone.setText(player.getPhone());
        playerEmail.setText(player.getEmail());
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
