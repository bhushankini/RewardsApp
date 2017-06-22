package com.bpk.rewards.utility;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by bkini on 5/26/17.
 */

public class Utils {


    public static String localTime(long utcTime) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm a"); //this format changeable
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
        Log.e("LOG","KHU last "+fmt.format(d1));

        Log.e("LOG","KHU SER "+fmt.format(d2));
        return !fmt.format(d1).equals(fmt.format(d2));
    }

    public static String getUserId(Context context) {
        return PrefUtils.getFromPrefs(context, Constants.USER_ID, "GUEST");
    }
}
