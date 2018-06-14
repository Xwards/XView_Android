package com.xwards.xview.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.xwards.xview.R;
import com.xwards.xview.dialogs.DialogInterfaces;
import com.xwards.xview.dialogs.GeneralDialog;
import com.xwards.xview.rest.APIRepository;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Nithinjith on 11/4/2017.
 * Base For All Activity
 * A level of Abstraction
 */

public class BaseActivity extends AppCompatActivity {

    public static final String DEVICE_DISPLAY_OFF = "display_off_from_power";
    public static final String BROADCAST_BATTERY_DATA_UPDATE = "battery_data_update_broadcast";
    protected final int SITE_REQ_CODE_FOR_AD = 10008;
    protected final int SITE_REQ_CODE_FOR_EVENT = 10009;
    public CompositeDisposable mCompositeDisposable;
    protected APIRepository mApiRepository;
    protected String DEVICE_ORIENTATION_KEY = "Orientation_Key";
    protected String WEB_VIEW_URL_KEY = "Selected_web_url";
    protected String SPEND_HOURS = "Spend_Hours";
    protected String WEBSITE_URL = "selected_website_url";
    private SweetAlertDialog dialog;
    private String TAG = "BaseActivity";
    private GeneralDialog mDialogFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState,
                         @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);

    }

    /**
     * Check Whether the Network connection available in the System OR Not
     *
     * @param context - Application Context
     * @return - true if yes, else false
     */

    protected boolean isNetworkConnectionAvailable(Context context) {
        try {
            if (context != null) {
                boolean haveConnectedWifi = false;
                boolean haveConnectedMobile = false;

                ConnectivityManager cm = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo[] netInfo = cm.getAllNetworkInfo();
                for (NetworkInfo ni : netInfo) {
                    if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                        if (ni.isConnected())
                            haveConnectedWifi = true;
                    if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                        if (ni.isConnected())
                            haveConnectedMobile = true;
                }
                return haveConnectedWifi || haveConnectedMobile;
            }
            return false;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return false;
    }

    protected void displayNetworkErrorDialog() {
        displayAlertDialog(getString(R.string.alert_dialog_no_network_msg1),
                false);
    }

    /**
     * @param message                - Message need to display
     * @param isCancelButtonRequired - Define if the cancel button required in the alert or not
     */
    protected void displayAlertDialog(String message, boolean isCancelButtonRequired) {
        FragmentManager fm = getSupportFragmentManager();
        if (mDialogFragment != null) {
            mDialogFragment.dismiss();
        }
        mDialogFragment = new GeneralDialog();
        Bundle data = new Bundle();
        data.putString(DialogInterfaces.BUNDLE_DIALOG_MESSAGE, message);
        data.putBoolean(DialogInterfaces.BUNDLE_DIALOG_CANCEL_REQUIRED, isCancelButtonRequired);
        data.putBoolean(DialogInterfaces.BUNDLE_DIALOG_IS_FROM_FRAGMENT, false);
        mDialogFragment.setArguments(data);
        mDialogFragment.show(fm, "TAG");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    public void showProgressDialog(String msg) {
        if (dialog != null) {
            dialog.setTitle(msg);
            dialog.show();
        }
    }

    public void hideProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * Returns the unique identifier for the device
     *
     * @return unique identifier for the device
     */
    @SuppressLint("MissingPermission")
    public String getDeviceIMEI() {
        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            deviceUniqueIdentifier = tm.getDeviceId();
        }
        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        return deviceUniqueIdentifier;
    }


    public String getMacAddress() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().
                getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        return wInfo.getMacAddress();
    }

}
