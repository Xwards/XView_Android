package com.xwards.xview.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.xwards.xview.base.BaseActivity;
import com.xwards.xview.home.MainActivity;

import static com.xwards.xview.base.BaseActivity.DEVICE_DISPLAY_OFF;


/**
 * Created by Nithin on 21-03-2018.
 * A Receiver for Getting the Power Disconnection Status
 */

public class PowerConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
            int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

            if (!isCharging && !usbCharge && !acCharge) {
                Intent sendIntent = new Intent(BaseActivity.BROADCAST_BATTERY_DATA_UPDATE);
                Bundle bundle = new Bundle();
                bundle.putBoolean(DEVICE_DISPLAY_OFF, true);
                sendIntent.putExtras(bundle);
                LocalBroadcastManager.getInstance(context).sendBroadcast(sendIntent);
            }
        } catch (Exception e) {
            Log.e("PowerConnectionReceiver", e.getMessage());
        }
    }
}
