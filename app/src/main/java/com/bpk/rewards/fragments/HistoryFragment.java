package com.bpk.rewards.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bpk.rewards.R;
import com.bpk.rewards.adapter.HistoryAdapter;
import com.bpk.rewards.model.UserTransaction;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by bkini on 6/18/17.
 */

public class HistoryFragment extends Fragment {

    private HistoryAdapter adapter;
    private ArrayList<UserTransaction> userHistory = new ArrayList<>();
    private RecyclerView recycler;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        recycler = (RecyclerView) root.findViewById(R.id.lb_recycler_view);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setHasFixedSize(true);
    }

    private void updateUserHistory() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(UserTransaction.FIREBASE_TRANSACTION_ROOT);
     //   Query queryRef = ref;//.orderByChild("timestamp");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Log.d("TAG","KKKKK Clear History "+userHistory.size());
                userHistory.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Log.d("TAG","KKKKK size "+userHistory.size());

                    UserTransaction txn = ds.getValue(UserTransaction.class);
                    userHistory.add(txn);
                }
                 Collections.reverse(userHistory);
                adapter = new HistoryAdapter(userHistory);

                recycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TAG","KKKKK onResume");

        updateUserHistory();
    }
}