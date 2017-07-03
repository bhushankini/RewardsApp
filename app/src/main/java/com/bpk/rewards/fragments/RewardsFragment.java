package com.bpk.rewards.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bpk.rewards.R;
import com.bpk.rewards.adapter.RewardsAdapter;
import com.bpk.rewards.model.Rewards;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class RewardsFragment extends Fragment {


    private RewardsAdapter adapter;
    private ArrayList<Rewards> rewardsList = new ArrayList<>();
    private RecyclerView recycler;
    public RewardsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rewards, container, false);
        recycler = (RecyclerView) root.findViewById(R.id.rewards_recycler_view);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setHasFixedSize(true);
    }

    private void getRewardsList() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Rewards.FIREBASE_REWARDS_ROOT);
        ref.orderByChild("display").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rewardsList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Rewards txn = ds.getValue(Rewards.class);
                    rewardsList.add(txn);
                }

                adapter = new RewardsAdapter(getActivity(),rewardsList);
                recycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        getRewardsList();
    }
}