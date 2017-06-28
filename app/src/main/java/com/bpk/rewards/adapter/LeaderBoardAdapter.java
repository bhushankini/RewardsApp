package com.bpk.rewards.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bpk.rewards.R;
import com.bpk.rewards.model.User;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.LeaderBoardViewHolder> {

    private ArrayList<User> dataSet;

    public static class LeaderBoardViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtPoints;

        TextView txtRank;

        public LeaderBoardViewHolder(View itemView) {
            super(itemView);
            this.txtName = (TextView) itemView.findViewById(R.id.tv_name);
            this.txtPoints = (TextView) itemView.findViewById(R.id.tv_points);
            this.txtRank = (TextView) itemView.findViewById(R.id.tv_rank);

        }
    }

    public LeaderBoardAdapter(ArrayList<User> data) {
        this.dataSet = data;
    }

    @Override
    public LeaderBoardViewHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leaderboard, parent, false);

        return new LeaderBoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LeaderBoardViewHolder holder, final int listPosition) {
        TextView txtName = holder.txtName;
        TextView txtPoints = holder.txtPoints;
        TextView txtRank = holder.txtRank;

        txtName.setText(dataSet.get(listPosition).getName());
        txtPoints.setText("" + dataSet.get(listPosition).getPoints());
        txtRank.setText("#"+(listPosition+1));

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
