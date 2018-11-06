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
import com.example.pokerscorekeeper.model.PlayerScore;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerScoreCardAdapter  extends  RecyclerView.Adapter<PlayerScoreCardAdapter.ViewHolder>
 implements CursorUpdater<PlayerScore> {

    private Context context;
    private ArrayList<PlayerScore> scores;
    private OnCardClickedListener<PlayerScore> callback;

    public PlayerScoreCardAdapter(Context context, ArrayList<PlayerScore> scores, OnCardClickedListener callback) {
        this.context = context;
        this.scores = scores;
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.player_score_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PlayerScore s = scores.get(position);

        holder.playerName.setText(s.getPlayerName());
        holder.playerScore.setText(String.valueOf(s.getTotalScore()));
        holder.scoreInfo.setText(context.getString(R.string.label_player_score_info, s.getShowingUp(), s.getPlacing(), s.getHighHand(), s.getFinalTable()));
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    @Override
    public void setCursor(List<PlayerScore> items) {
        scores.clear();
        scores.addAll(items);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.playerName)
        TextView playerName;

        @BindView(R.id.playerScore)
        TextView playerScore;

        @BindView(R.id.playerScoreInfo)
        TextView scoreInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onCardClicked(scores.get(getAdapterPosition()));
                }
            });
        }
    }
}
