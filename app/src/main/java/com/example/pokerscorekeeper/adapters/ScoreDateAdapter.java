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
import com.example.pokerscorekeeper.model.ScoreDate;
import com.example.pokerscorekeeper.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScoreDateAdapter extends RecyclerView.Adapter<ScoreDateAdapter.ViewHolder>
 implements CursorUpdater<ScoreDate> {

    private Context context;
    private ArrayList<ScoreDate> dates;
    private SimpleDateFormat format;
    private OnCardClickedListener<ScoreDate> callback;

    public ScoreDateAdapter(Context context, ArrayList<ScoreDate> dates, OnCardClickedListener<ScoreDate> callback)
    {
        this.context = context;
        this.dates = dates;
        this.callback = callback;
        format = new SimpleDateFormat(DateUtils.DATE_FORMAT_DEFAULT);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.date_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScoreDate d = dates.get(position);

        holder.playerCount.setText(context.getString(R.string.label_player_count, d.getPlayers()));
        holder.scoreDate.setText(d.getDate());

        //Parse the date and format it
        try {
            long date = new SimpleDateFormat(DateUtils.DATE_FORMAT_SQL).parse(d.getDate()).getTime();
            holder.scoreDate.setText(format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            holder.scoreDate.setText(d.getDate());
        }

    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    @Override
    public void setCursor(List<ScoreDate> items) {
        dates.clear();
        dates.addAll(items);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.scoreDate)
        TextView scoreDate;

        @BindView(R.id.playerCount)
        TextView playerCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onCardClicked(dates.get(getAdapterPosition()));
                }
            });
        }
    }
}
