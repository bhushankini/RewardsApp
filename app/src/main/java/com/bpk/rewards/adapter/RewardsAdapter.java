package com.bpk.rewards.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpk.rewards.R;
import com.bpk.rewards.model.Rewards;

import java.util.ArrayList;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.RewardsViewHolder> {

    private ArrayList<Rewards> dataSet;
    private Context context;

    public static class RewardsViewHolder extends RecyclerView.ViewHolder {

        TextView txtDisplay;
        TextView txtValue;
        ImageView imgReward;


        public RewardsViewHolder(View itemView) {
            super(itemView);
            this.txtDisplay = (TextView) itemView.findViewById(R.id.tv_display);
            this.txtValue =(TextView) itemView.findViewById(R.id.tv_value);
            this.imgReward = (ImageView) itemView.findViewById(R.id.imgReward);
        }
    }

    public RewardsAdapter(Context context, ArrayList<Rewards> data) {
        this.dataSet = data;
        this.context = context;
    }

    @Override
    public RewardsViewHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rewards, parent, false);

        return new RewardsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RewardsViewHolder holder, final int listPosition) {
        TextView txtDisplay = holder.txtDisplay;
        TextView txtValue = holder.txtValue;

        ImageView imgReward =holder.imgReward;

        txtDisplay.setText(dataSet.get(listPosition).getDisplay());
        txtValue.setText(""+dataSet.get(listPosition).getValue());

        switch (dataSet.get(listPosition).getIcon()) {
            case 1:
                imgReward.setImageResource(R.drawable.amazon_icon);
               break;
            case 2:
                imgReward.setImageResource(R.drawable.paytm_icon);
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
