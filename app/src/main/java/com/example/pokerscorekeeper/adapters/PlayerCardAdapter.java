package com.example.pokerscorekeeper.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pokerscorekeeper.R;
import com.example.pokerscorekeeper.interfaces.CursorUpdater;
import com.example.pokerscorekeeper.interfaces.OnCardClickedListener;
import com.example.pokerscorekeeper.model.Player;
import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.ArrayList;
import java.util.List;

public class PlayerCardAdapter extends RecyclerView.Adapter<PlayerCardAdapter.ViewHolder> implements
        CursorUpdater<Player> {

    private Context context;
    private ArrayList<Player> players;
    private OnCardClickedListener<Player> callback;

    public PlayerCardAdapter(Context context, ArrayList<Player> players, OnCardClickedListener<Player> callback)
    {
        this.context = context;
        this.players = players;
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.player_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Player p = players.get(position);

        holder.playerName.setText(p.getName());
        holder.playerEmail.setText(p.getEmail());
        holder.playerScore.setText(String.valueOf(p.getTotalScore()));
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    @Override
    public void setCursor(List<Player> items) {
        players.clear();
        players.addAll(items);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.playerName)
        TextView playerName;

        @BindView(R.id.playerEmail)
        TextView playerEmail;

        @BindView(R.id.playerScore)
        TextView playerScore;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onCardClicked(players.get(getAdapterPosition()));
                }
            });
        }
    }
}
