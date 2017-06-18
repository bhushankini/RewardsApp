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
import com.bpk.rewards.utility.Constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class VideoFragment extends Fragment implements View.OnClickListener ,RewardedVideoAdListener {

    private String TAG = "REWARDS";
    Button btnDailyReward;
    Button btnAppLovin;
    Button btnAdmob;
    Button btnVungle;
    private RewardedVideoAd mRewardedVideoAd;
    private boolean admobLoaded = false;

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
            admobLoaded = false;
            mRewardedVideoAd.loadAd(Constants.ADMOB_AD_UNIT_ID, new AdRequest.Builder().addTestDevice("F98B32499B302F1D5145AF987EACC26E").build());
        }
    }

    private void showRewardedVideo() {
        btnAdmob.setEnabled(false);
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }
}
