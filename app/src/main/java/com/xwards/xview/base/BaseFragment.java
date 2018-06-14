package com.xwards.xview.base;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xwards.xview.dialogs.DialogInterfaces;
import com.xwards.xview.dialogs.GeneralDialog;

import dmax.dialog.SpotsDialog;

/**
 * Created by Nithinjith on 11/4/2017.
 * Base for All Fragments
 * A level of Abstraction
 */

public class BaseFragment extends Fragment {


    private static final String TAG = "BaseFragment";
    private GeneralDialog mDialogFragment;
    private FragmentManager mFragmentManager;
    private SpotsDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getActivity().getSupportFragmentManager();
        dialog = new SpotsDialog(getActivity());
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * Check the device is connected to network or not:
     *
     * @param context ; Application context
     * @return :: True if The network available - Else false
     */
    protected boolean isNetworkConnectionAvailable(Context context) {
        try {
            if (context != null) {
                boolean haveConnectedWifi = false;
                boolean haveConnectedMobile = false;

                ConnectivityManager cm = (ConnectivityManager) getActivity().
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

    /**
     * @param message                - Message need to display
     * @param isCancelButtonRequired - Define if the cancel button required in the alert or not
     */
    protected void displayAlertDialog(String message, boolean isCancelButtonRequired) {
        if (mFragmentManager != null) {
            if (mDialogFragment != null) {
                mDialogFragment.dismiss();
            }
            mDialogFragment = new GeneralDialog();
            Bundle data = new Bundle();
            data.putString(DialogInterfaces.BUNDLE_DIALOG_MESSAGE, message);
            data.putBoolean(DialogInterfaces.BUNDLE_DIALOG_CANCEL_REQUIRED, isCancelButtonRequired);
            data.putBoolean(DialogInterfaces.BUNDLE_DIALOG_IS_FROM_FRAGMENT, true);
            mDialogFragment.setArguments(data);
            mDialogFragment.setTargetFragment(this, DialogInterfaces.DIALOG_REQUESTED_CODE);
            mDialogFragment.show(mFragmentManager, "TAG");
        }
    }

    public void showProgressDialog(String msg) {

        dialog.show();
        dialog.setMessage(msg);

    }

    public void hideProgressDialog() {
        dialog.dismiss();
    }
}
