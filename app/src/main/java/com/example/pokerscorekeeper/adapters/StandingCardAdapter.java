package com.example.pokerscorekeeper.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pokerscorekeeper.R;
import com.example.pokerscorekeeper.interfaces.CursorUpdater;
import com.example.pokerscorekeeper.interfaces.OnCardClickedListener;
import com.example.pokerscorekeeper.model.PlayerStanding;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StandingCardAdapter extends RecyclerView.Adapter<StandingCardAdapter.ViewHolder>
implements CursorUpdater<PlayerStanding> {

    private Context context;
    private ArrayList<PlayerStanding> standings;
    private OnCardClickedListener<PlayerStanding> callback;

    public StandingCardAdapter(Context context, ArrayList<PlayerStanding> standings, OnCardClickedListener<PlayerStanding> callback) {
        this.context = context;
        this.standings = standings;
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.standing_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PlayerStanding s = standings.get(position);

        holder.playerName.setText(s.getName());
        holder.playerScore.setText(String.valueOf(s.getStandingScore()));
    }

    @Override
    public int getItemCount() {
        return standings.size();
    }

    @Override
    public void setCursor(List<PlayerStanding> items) {
        standings.clear();
        standings.addAll(items);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.playerName)
        TextView playerName;

        @BindView(R.id.playerScore)
        TextView playerScore;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onCardClicked(standings.get(getAdapterPosition()));
                }
            });
        }
    }
}
