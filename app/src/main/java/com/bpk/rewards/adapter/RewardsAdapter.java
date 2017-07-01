package com.bpk.rewards.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bpk.rewards.R;
import com.bpk.rewards.model.Rewards;

import java.util.ArrayList;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.RewardsViewHolder> {

    private ArrayList<Rewards> dataSet;

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

    public RewardsAdapter(ArrayList<Rewards> data) {
        this.dataSet = data;
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

        ImageView imgReward = holder.imgReward;

        txtDisplay.setText(dataSet.get(listPosition).getDisplay());
        txtValue.setText(""+dataSet.get(listPosition).getValue());

        switch (dataSet.get(listPosition).getIcon()) {
            case 1://amazon
                imgReward.setImageResource(R.drawable.amazon_icon);
               break;
            case 2:///paytm
                imgReward.setImageResource(R.drawable.paytm_icon);
                break;
            case 101://airtel
                imgReward.setImageResource(R.drawable.aircel_icon);
                break;
            case 102://airtel
                imgReward.setImageResource(R.drawable.airtel_icon);
                break;

            case 103://bsnl
                imgReward.setImageResource(R.drawable.bsnl_logo);
                break;
            case 104://idea
                imgReward.setImageResource(R.drawable.idea_icon);
                break;
            case 105: //vodafone
                imgReward.setImageResource(R.drawable.vodafone_icon);
                break;
            case 106: //Jio
                imgReward.setImageResource(R.drawable.jio_icon);
                break;
        }
        txtValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dataSet.get(listPosition).getIcon() > 100){
                    Log.d("TAG","AAAAAAA Mobile recharge ask mobile "+dataSet.get(listPosition).getDisplay());
                } else {
                    Log.d("TAG","AAAAAAA Gift card ask email "+dataSet.get(listPosition).getDisplay());

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
