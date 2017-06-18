package com.bpk.rewards;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bpk.rewards.interfaces.ServerTimeAsyncResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by bkini on 6/17/17.
 */

public class SplashActivity extends Activity implements ServerTimeAsyncResponse {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                GetServerFromTime task = new GetServerFromTime(SplashActivity.this);
                task.execute();
            }
        }, 1500);

    }

    private void gotoMain(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!= null ){

            startActivity(new Intent(this, MainActivity.class));
        }
        else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }

    @Override
    public void processFinish(String result) {
            gotoMain();
     }
}


class GetServerFromTime extends AsyncTask<String, Void, String> {

    public ServerTimeAsyncResponse delegate = null;//Call back interface

    public GetServerFromTime(ServerTimeAsyncResponse asyncResponse) {
        delegate = asyncResponse;//Assigning call back interfacethrough constructor
    }

    @Override
    protected String doInBackground(String... urls) {
        // we use the OkHttp library from https://github.com/square/okhttp
        OkHttpClient client = new OkHttpClient();
        Request request =
                new Request.Builder()
                        .url("http://mobilemauj.com/mangela/servertime.php")
                        .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("KHUSHI", " KHUSHISSSS time " + result);
        delegate.processFinish(result);
    }
}

