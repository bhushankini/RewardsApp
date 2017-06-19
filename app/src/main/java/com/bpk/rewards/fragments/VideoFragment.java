package com.bpk.rewards.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bpk.rewards.R;
import com.bpk.rewards.model.User;
import com.bpk.rewards.model.UserTransaction;
import com.bpk.rewards.utility.Constants;
import com.bpk.rewards.utility.PrefUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VideoFragment extends Fragment implements View.OnClickListener ,RewardedVideoAdListener {

    private String TAG = "REWARDS";
    Button btnDailyReward;
    Button btnAppLovin;
    Button btnAdmob;
    Button btnVungle;
    private RewardedVideoAd mRewardedVideoAd;
    private DatabaseReference mFirebaseUserDatabase;
    private DatabaseReference mFirebaseTransactionDatabase;
    private FirebaseDatabase mFirebaseInstance;
    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_video, container, false);
        btnDailyReward = (Button) view.findViewById(R.id.btnDailyReward);
        btnDailyReward.setOnClickListener(this);

        btnAppLovin = (Button) view.findViewById(R.id.btnAppLovin);
        btnAppLovin.setOnClickListener(this);

        btnAdmob = (Button) view.findViewById(R.id.btnAdmob);
        btnAdmob.setOnClickListener(this);

        btnVungle = (Button) view.findViewById(R.id.btnVungle);
        btnVungle.setOnClickListener(this);
        MobileAds.initialize(getActivity(), Constants.ADMOB_APP_ID);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseUserDatabase = mFirebaseInstance.getReference(User.FIREBASE_USER_ROOT);
        mFirebaseTransactionDatabase = mFirebaseInstance.getReference(UserTransaction.FIREBASE_TRANSACTION_ROOT);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnDailyReward:
                Toast.makeText(getActivity(),"Claim Reward ",Toast.LENGTH_LONG).show();
                break;
            case  R.id.btnAdmob:
                Toast.makeText(getActivity(),"Admob ",Toast.LENGTH_LONG).show();
                showRewardedVideo();

                break;
            case R.id.btnAppLovin:
                Toast.makeText(getActivity(),"App Lovin ",Toast.LENGTH_LONG).show();

                break;
            case  R.id.btnVungle:
                Toast.makeText(getActivity(),"Vungle ",Toast.LENGTH_LONG).show();
                break;
            default:
        }
    }

    //Admob Start
    @Override
    public void onRewardedVideoAdLoaded() {
        Log.d(TAG,"onRewardedVideoAdLoaded");
        btnAdmob.setEnabled(true);
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Log.d(TAG,"onRewardedVideoAdOpened");
    }

    @Override
    public void onRewardedVideoStarted() {
        Log.d(TAG,"onRewardedVideoStarted");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Log.d(TAG,"onRewardedVideoAdClosed");
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Log.d(TAG,"onRewarded");
        rewardsPoints(2,"Admob", "Video");
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Log.d(TAG,"onRewardedVideoAdLeftApplication");
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Log.d(TAG,"onRewardedVideoAdFailedToLoad");
    }

    //Admob End


    @Override
    public void onResume() {
        super.onResume();
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
    }

    private void loadRewardedVideoAd() {
        if (!mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.loadAd(Constants.ADMOB_AD_UNIT_ID, new AdRequest.Builder().addTestDevice("F98B32499B302F1D5145AF987EACC26E").build());
        }
    }

    private void showRewardedVideo() {
        btnAdmob.setEnabled(false);
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }


    private void rewardsPoints(final int points, final String source, final String type) {
        Log.d(TAG, "KHUSHI updatePoints ");
        final String userId = PrefUtils.getFromPrefs(getActivity(), Constants.USER_ID, "unknownuser");
        Log.d(TAG, "KHUSHI updatePoints userId "+userId);
        mFirebaseUserDatabase.child(userId).child("points").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "KHUSHI snapshot " + dataSnapshot.getValue());
                if (dataSnapshot.getValue() != null) {
                    long totalPoints = (long) dataSnapshot.getValue();
                    mFirebaseUserDatabase.child(userId).child("points").setValue(totalPoints + points);
                    UserTransaction ut = new UserTransaction();
                    ut.setSource(source);
                    ut.setPoints(points);
                    ut.setType(type);
                    //      ut.toMap();
                    mFirebaseTransactionDatabase.child(userId).push().setValue(ut.toMap());
                } else {
                    mFirebaseUserDatabase.child(userId).child("points").setValue(points);
                    // txtPoints.setText(points + "  "); //update points label

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //
    }

}
