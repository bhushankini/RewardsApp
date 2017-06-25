package com.bpk.rewards.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.bpk.rewards.LoginActivity;
import com.bpk.rewards.R;
import com.bpk.rewards.utility.Utils;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by bkini on 6/18/17.
 */

public class AccountFragment extends Fragment {

    private Button btnLogout;
    private Button btnShare;
    private Button btnRetry;
    private RelativeLayout rlMain;
    private RelativeLayout rlNoConnection;
    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rlMain = (RelativeLayout) view.findViewById(R.id.rl_main);
        rlNoConnection = (RelativeLayout) view.findViewById(R.id.rl_noconnection);
        btnLogout = (Button)view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Sign Out!")
                        .setCustomImage(R.mipmap.ic_launcher)
                        .setConfirmText("Signout")
                        .setCancelText("Cancel")
                        .showCancelButton(true)
                        .setContentText("Are you sure you want to signout?")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                FirebaseAuth.getInstance().signOut();
                                LoginManager.getInstance().logOut();
                                startActivity(new Intent(getActivity(),LoginActivity.class));
                                getActivity().finish();

                            }
                        })
                        .show();
            }
        });

        btnShare = (Button) view.findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareDialog shareDialog;
               // FacebookSdk.sdkInitialize(getApplicationContext());
                shareDialog = new ShareDialog(getActivity());

                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle("My Rewards")
                        .setContentDescription(
                                "\"Some more descriptve text\"")
                        .setQuote("Some text here in quote")
                        .setContentUrl(Uri.parse("https:mobilemauj.com"))
                        .build();

                shareDialog.show(linkContent);

            }
        });
        btnRetry = (Button) view.findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        checkConnection();
    }

    private void checkConnection(){
        if(Utils.isNetworkAvailable(getActivity())){
            rlMain.setVisibility(View.VISIBLE);
            rlNoConnection.setVisibility(View.GONE);
        } else {
            rlMain.setVisibility(View.GONE);
            rlNoConnection.setVisibility(View.VISIBLE);

        }
    }
}