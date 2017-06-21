package com.bpk.rewards.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bpk.rewards.R;
import com.bpk.rewards.model.UserTransaction;
import com.bpk.rewards.utility.Utils;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private ArrayList<UserTransaction> dataSet;

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView txtSource;
        TextView txtPoints;
        TextView txtTime;


        public HistoryViewHolder(View itemView) {
            super(itemView);
            this.txtSource = (TextView) itemView.findViewById(R.id.tv_source);
            this.txtPoints = (TextView) itemView.findViewById(R.id.tv_points);
            this.txtTime = (TextView) itemView.findViewById(R.id.tv_timestamp);
        }
    }

    public HistoryAdapter(ArrayList<UserTransaction> data) {
        Log.d("TAG", "Jyoti HistoryAdapter inside " + data.size());

        this.dataSet = data;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        Log.d("TAG", "Jyoti onCreateViewHolder inside ");

        HistoryViewHolder myViewHolder = new HistoryViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final HistoryViewHolder holder, final int listPosition) {
        Log.d("TAG", "Jyoti onBindViewHolder " + listPosition);
        TextView txtSource = holder.txtSource;
        TextView txtPoints = holder.txtPoints;
        TextView txtTime = holder.txtTime;


        txtSource.setText(dataSet.get(listPosition).getSource() + " " + dataSet.get(listPosition).getType());
        txtPoints.setText("" + dataSet.get(listPosition).getPoints());
        if (dataSet.get(listPosition).getPoints() > 0) {
            txtPoints.setTextColor(Color.parseColor("#006600"));
            txtPoints.setText("+" + dataSet.get(listPosition).getPoints());

        } else {
            txtPoints.setTextColor(Color.RED);
        }

        txtTime.setText(Utils.localTime(dataSet.get(listPosition).getTimestamp()));
    }

    @Override
    public int getItemCount() {
        Log.d("TAG", "SAMP getItemCount " + dataSet.size());
        return dataSet.size();
    }

}
