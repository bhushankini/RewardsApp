package com.bpk.rewards.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bpk.rewards.LoginActivity;
import com.bpk.rewards.R;
import com.bpk.rewards.utility.Constants;
import com.bpk.rewards.utility.PrefUtils;
import com.bpk.rewards.utility.Utils;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
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
    private RelativeLayout rlEmailVerification;
    private CircularImageView imgProfile;
    private TextView txtUserName;
    private TextView txtUserEmail;
    private TextView txtSendVerificationMail;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.sign_out))
                        .setCustomImage(R.mipmap.ic_launcher)
                        .setConfirmText(getString(R.string.sign_out))
                        .setCancelText(getString(R.string.cancel))
                        .showCancelButton(true)
                        .setContentText("Are you sure you want to signout?")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                PrefUtils.removeFromPrefs(getActivity(), Constants.REFERRER_ID);
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
        imgProfile = (CircularImageView) view.findViewById(R.id.img_profile);
        txtUserName = (TextView) view.findViewById(R.id.txtUserName);
        txtUserEmail= (TextView) view.findViewById(R.id.txtUserEmail);
        txtSendVerificationMail = (TextView) view.findViewById(R.id.txtSendVerificationMail);
        rlEmailVerification = (RelativeLayout) view.findViewById(R.id.rl_email_verification);
        txtSendVerificationMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getActivity(),"Send Verification Email",Toast.LENGTH_LONG).show();
                sendEmailVerification();
            }
        });
        updateUI();

    }

    @Override
    public void onResume() {
        super.onResume();
        checkConnection();
    }

    private void updateUI(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null && !currentUser.isEmailVerified()) {
            rlEmailVerification.setVisibility(View.VISIBLE);
        }
        else {
            rlEmailVerification.setVisibility(View.GONE);
        }

        txtUserEmail.setText(currentUser.getEmail());
        txtUserName.setText(currentUser.getDisplayName());

        if(currentUser.getPhotoUrl()!=null)
        Picasso.with(getActivity()).load(currentUser.getPhotoUrl()).placeholder(R.drawable.user_icon).into(imgProfile);

    }

    private void sendEmailVerification() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(),
                                        "Verification email sent to " + user.getEmail(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("TAG", "sendEmailVerification", task.getException());
                                Toast.makeText(getActivity(),
                                        "Failed to send verification email.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
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