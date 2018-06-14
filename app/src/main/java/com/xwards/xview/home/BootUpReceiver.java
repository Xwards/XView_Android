package com.xwards.xview.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xwards.xview.sp.SharedPrefDataSupplier;
import com.xwards.xview.splash.SplashActivity;

/**
 * Created by Nithinjith on 11/19/2017.
 * This is the Boot Loader of this Application
 */

public class BootUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPrefDataSupplier mSupplier = new SharedPrefDataSupplier(context);

        if (mSupplier == null || mSupplier.getSelectedLocation() == -1) {
            Intent i = new Intent(context, SplashActivity.class);
            //MyActivity can be anything which you want to start on bootup...
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else {
            Intent i = new Intent(context, MainActivity.class);
            //MyActivity can be anything which you want to start on bootup...
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
