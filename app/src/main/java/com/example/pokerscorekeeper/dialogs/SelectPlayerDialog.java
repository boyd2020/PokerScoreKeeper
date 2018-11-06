package com.example.pokerscorekeeper.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pokerscorekeeper.activities.AddPlayerActivity;
import com.example.pokerscorekeeper.R;
import com.example.pokerscorekeeper.adapters.PlayerCardAdapter;
import com.example.pokerscorekeeper.interfaces.OnCardClickedListener;
import com.example.pokerscorekeeper.managers.PlayerManager;
import com.example.pokerscorekeeper.model.Player;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SelectPlayerDialog extends DialogFragment implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>, OnCardClickedListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Unbinder unbinder;
    private PlayerManager manager;
    private PlayerCardAdapter adapter;
    private OnPlayerSelectedListener callback;


    public interface OnPlayerSelectedListener {
        void onPlayerSelected(Player player);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnPlayerSelectedListener) getTargetFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();

        if(d != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.select_dialog, container, false);
        unbinder = ButterKnife.bind(this, v);

        manager = new PlayerManager(getContext());
        adapter = new PlayerCardAdapter(getContext(), new ArrayList<Player>(), this);

        recyclerView.setAdapter(adapter);
        getActivity().getSupportLoaderManager().initLoader(100, null, this);

        return v;
    }

    @OnClick({R.id.dialogCreate, R.id.dialogCancel})
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.dialogCreate:
                Intent intent = new Intent(getContext(), AddPlayerActivity.class);
                startActivity(intent);
                dismiss();
                break;

            case R.id.dialogCancel:
                dismiss();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return manager.getAllPlayers();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ArrayList<Player> players = manager.getPlayersFromCursor(data);
        adapter.setCursor(players);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onCardClicked(Object object) {
        callback.onPlayerSelected((Player)object);
        dismiss();
    }
}
