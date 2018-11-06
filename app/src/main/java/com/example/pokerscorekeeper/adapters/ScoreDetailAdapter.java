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
import com.example.pokerscorekeeper.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScoreDetailAdapter extends RecyclerView.Adapter<ScoreDetailAdapter.ViewHolder>
implements CursorUpdater {

    private Context context;
    private SimpleDateFormat format;
    private ArrayList<PlayerScore> scores;
    private OnCardClickedListener<PlayerScore> callback;

    public ScoreDetailAdapter(Context context, ArrayList<PlayerScore> scores, OnCardClickedListener<PlayerScore> callback) {
        this.context = context;
        this.scores = scores;
        this.callback = callback;
        format = new SimpleDateFormat(DateUtils.DATE_FORMAT_DEFAULT);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.date_score_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PlayerScore s = scores.get(position);

        holder.playerScore.setText(String.valueOf(s.getTotalScore()));
        holder.scoreInfo.setText(context.getString(R.string.label_player_score_info, s.getShowingUp(), s.getPlacing(), s.getHighHand(), s.getFinalTable()));

        //Parse the date and format it
        try {
            long date = new SimpleDateFormat(DateUtils.DATE_FORMAT_SQL).parse(s.getDate()).getTime();
            holder.scoreDate.setText(format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            holder.scoreDate.setText(s.getDate());
        }
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    @Override
    public void setCursor(List items) {
        scores.clear();
        scores.addAll(items);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.playerScore)
        TextView playerScore;

        @BindView(R.id.playerScoreInfo)
        TextView scoreInfo;

        @BindView(R.id.scoreDate)
        TextView scoreDate;

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
