package com.bpk.rewards.utility;

import android.content.Context;

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
        Date lastOpen = new Date(lastOpenTime);
        Date current = new Date(serverTime);
        return lastOpen.after(current);
    }

    public static String getUserId(Context context) {
        return PrefUtils.getFromPrefs(context, Constants.USER_ID, "GUEST");
    }
}
