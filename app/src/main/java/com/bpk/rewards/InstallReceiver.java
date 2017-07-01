package com.bpk.rewards;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.bpk.rewards.utility.Constants;
import com.bpk.rewards.utility.PrefUtils;

public class InstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if(action != null && action.equals("com.android.vending.INSTALL_REFERRER")){
            try {
                final String referrer = intent.getStringExtra("referrer");
                Log.e("InstallReceiver", "referrer == "+referrer);
                PrefUtils.saveToPrefs(context, Constants.REFERRER_ID, referrer);
            } catch (Exception e) {
                Log.e("InstallReceiver", "Error "+e);
            }
        }
    }
}
//am broadcast -a com.android.vending.INSTALL_REFERRER -n com.bpk.rewards/.InstallReceiver --es "referrer" "pAsMEu9uKDeuIJuPPqweNXmJb3Z2"
//https://play.google.com/store/apps/details?id=com.bpk.rewards&referrer=ABCDEFGHIJKLMNO
