package com.bpk.rewards;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.bpk.rewards.utility.Constants;
import com.bpk.rewards.utility.PrefUtils;

public class InstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        Bundle extras = intent.getExtras();
        String referrerString = null;
     /*   try {
            if (extras != null) {
                referrerString = extras.getString(Intent.EXTRA_REFERRER);
                Log.e("InstallReceiver", "HHHHH referer string "+referrerString);
                PrefUtils.saveToPrefs(context, Constants.REFERRER_ID, referrerString);
            }
        }catch (Exception e){
            Log.e("InstallReceiver", "Error "+e);
        }
*/
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
//am broadcast -a com.android.vending.INSTALL_REFERRER -n com.bpk.rewards/.InstallReceiver --es "referrer" "Rjtg8Wgf1xOinpD1wiQL9ZxvPN62"
//https://play.google.com/store/apps/details?id=com.bpk.rewards&referrer=ABCDEFGHIJKLMNO
//PT    IfXtTDhgWqDIyRSrK2Rx2S7773
//pAsMEu9uKDeuIJuPPqweNXmJb3Z2

//  keytool -exportcert -alias release -keystore keystore.jks
//keytool -exportcert -alias release -keystore keystore.jks | openssl sha1 -binary | openssl base64