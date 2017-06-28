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
import com.bpk.rewards.adapter.LeaderBoardAdapter;
import com.bpk.rewards.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by bkini on 6/18/17.
 */

public class LeaderBoardFragment extends Fragment {
    private LeaderBoardAdapter adapter;
    private ArrayList<User> userArrayList = new ArrayList<>();
    private RecyclerView recycler;
    public LeaderBoardFragment() {
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

    private void updateLeaderBoard() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(User.FIREBASE_USER_ROOT).getParent();
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                userArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    userArrayList.add(user);
                }
                adapter = new LeaderBoardAdapter(getActivity(),userArrayList);
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
        updateLeaderBoard();
    }
}