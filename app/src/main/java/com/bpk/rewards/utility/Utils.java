package com.bpk.rewards.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.bpk.rewards.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by bkini on 5/26/17.
 */

public class Utils {


    public static String localTime(long utcTime) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm a"); //this format changeable
        dateFormatter.setTimeZone(TimeZone.getDefault());
        return dateFormatter.format(utcTime);
    }

    public static String localDate(long utcTime) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy"); //this format changeable
        dateFormatter.setTimeZone(TimeZone.getDefault());
        return dateFormatter.format(utcTime);
    }

    public static long getCurrentDateTime() {
        return new Date().getTime();
    }


    public static boolean isNewDate(long lastOpenTime, long serverTime) {

        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        Date d1 = new Date(lastOpenTime);
        Date d2 = new Date(serverTime*1000);
        Date today = new Date();
        Log.e("LOG","KHUSHI today "+today.getTime());
        Log.e("LOG","KHUSHI today formated "+fmt.format(today));
        Log.e("LOG","KHU last "+fmt.format(d1));

        Log.e("LOG","KHU SER "+fmt.format(d2));
        return !fmt.format(d1).equals(fmt.format(d2));
    }

    public static String getUserId(Context context) {
        return PrefUtils.getFromPrefs(context, Constants.USER_ID, "GUEST");
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showRewardsDialog(Context context, int points){
        new SweetAlertDialog(context, SweetAlertDialog.BUTTON_POSITIVE)
                .setTitleText("Congratulations!!!")
                .setCustomImage(R.mipmap.ic_launcher)
                .setContentText("Congratulations you got "+points+ " points")
                .show();
    }
}
