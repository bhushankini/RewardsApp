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
import com.bpk.rewards.utility.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.vungle.publisher.EventListener;
import com.vungle.publisher.VunglePub;

public class VideoFragment extends Fragment implements View.OnClickListener ,RewardedVideoAdListener {

    private String TAG = "REWARDS";
    private Button btnDailyReward;
    private Button btnAppLovin;
    private Button btnAdmob;
    private Button btnVungle;
    private RewardedVideoAd mRewardedVideoAd;
    private DatabaseReference mFirebaseUserDatabase;
    private DatabaseReference mFirebaseTransactionDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private final VunglePub vunglePub = VunglePub.getInstance();

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate Video");
        // initialize the Publisher SDK
        vunglePub.init(getActivity(), Constants.VUNGLE_APP_ID);
        vunglePub.setEventListeners(vungleAdListener);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG,"onCreateView Video");

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
                long serverTime = PrefUtils.getFromPrefs(getActivity(), Constants.SERVER_TIME, (long) 0.0);
                long lastDailyReward = PrefUtils.getFromPrefs(getActivity(), Constants.LAST_DAILY_REWARD, (long) 0.0);
                boolean dailyReward = Utils.isNewDate(lastDailyReward,serverTime);

                if(dailyReward){
                    Toast.makeText(getActivity(),"Claim Reward ",Toast.LENGTH_LONG).show();
                    rewardsPoints(15,"Daily", "Reward");
                    PrefUtils.saveToPrefs(getActivity(), Constants.LAST_DAILY_REWARD, serverTime);

                    mFirebaseUserDatabase.child(Utils.getUserId(getActivity())).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.getValue() != null) {
                                Log.d(TAG, "KHUSHI111 snapshot setting last open" + dataSnapshot.getValue());
                                mFirebaseUserDatabase.child(Utils.getUserId(getActivity())).child("lastopen").setValue(ServerValue.TIMESTAMP);


                            } else {
                                Log.d(TAG, "KHUSHI111 snapshot Error setting lastopne");

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                } else {
                    Toast.makeText(getActivity(),"Already Claimed",Toast.LENGTH_LONG).show();

                }

                break;
            case  R.id.btnAdmob:
                Toast.makeText(getActivity(),"Admob ",Toast.LENGTH_LONG).show();
                showRewardedVideo();

                break;
            case R.id.btnAppLovin:
                Toast.makeText(getActivity(),"App Lovin ",Toast.LENGTH_LONG).show();

                break;
            case  R.id.btnVungle:
                if(vunglePub.isAdPlayable()) {
                    vunglePub.playAd();
                } else {
                    Toast.makeText(getActivity(),"Video is not ready",Toast.LENGTH_LONG).show();
                }
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
            //Nexus 5
          //  mRewardedVideoAd.loadAd(Constants.ADMOB_AD_UNIT_ID, new AdRequest.Builder().addTestDevice("F98B32499B302F1D5145AF987EACC26E").build());

            //Moto g
            mRewardedVideoAd.loadAd(Constants.ADMOB_AD_UNIT_ID, new AdRequest.Builder().addTestDevice("56480886047D624B5EC3065A430E7E04").build());



        }
    }

    private void showRewardedVideo() {
        btnAdmob.setEnabled(false);
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }


    //Vungle
    private final EventListener vungleAdListener = new EventListener() {
        @Override
        public void onAdEnd(boolean b, boolean b1) {
            Log.d(TAG, "KHUSHI Vungle ad ended b= "+b+ "  b1= "+b1  );
            if(b){
                rewardsPoints(2,"Vungle", "Video");
            }
        }

        @Override
        public void onAdStart() {
            Log.d(TAG, "KHUSHI Vungle onAdStart " );

        }

        @Override
        public void onAdUnavailable(String s) {
            Log.d(TAG, "KHUSHI Vungle ad onAdUnavailable " );

        }

        @Override
        public void onAdPlayableChanged(boolean b) {
            Log.d(TAG, "KHUSHI Vungle ad onAdPlayableChanged " );

        }

        @Override
        public void onVideoView(boolean b, int i, int i1) {
            Log.d(TAG, "KHUSHI Vungle ad onVideoView " );

        }
    };

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
