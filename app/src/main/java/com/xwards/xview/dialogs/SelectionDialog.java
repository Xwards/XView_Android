package com.xwards.xview.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.xwards.xview.R;
import com.xwards.xview.splash.LocationData;
import com.xwards.xview.splash.LocationModel;

/**
 * Created by Nithinjith on 11/5/2017.
 */

public class SelectionDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String message = getArguments().getString(DialogInterfaces.BUNDLE_DIALOG_MESSAGE);
        final boolean isCancelRequired = getArguments().getBoolean(DialogInterfaces.BUNDLE_DIALOG_CANCEL_REQUIRED);
        final boolean isFromFragment = getArguments().getBoolean(DialogInterfaces.BUNDLE_DIALOG_IS_FROM_FRAGMENT);
        final LocationData locationData = getArguments().getParcelable(DialogInterfaces.BUNDLE_DIALOG_LOCATION_DATA);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle(message);
//        dialog.setMessage(message);
        final CharSequence[] item = new CharSequence[locationData.getLocationResp().getLocationList().size()];
        if (message.equalsIgnoreCase(getString(R.string.alert_dialog_location_message))) {
            for (int i = 0; i < locationData.getLocationResp().getLocationList().size(); i++) {
                item[i] = locationData.getLocationResp().getLocationList().get(i).getLocationName();
            }
        }
        dialog.setSingleChoiceItems(item, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (LocationModel obj : locationData.getLocationResp().getLocationList()) {
                    if (obj.getLocationName().equalsIgnoreCase((String) item[i])) {
                        sendResultToActivity(obj);
                        break;
                    }
                }
            }
        });
        if (isCancelRequired) {
            dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }
        return dialog.create();
    }

    private void sendResultToActivity(LocationModel locationId) {
        SelectionDialogCallBack mCallBack = (SelectionDialogCallBack) getActivity();
        if (mCallBack != null) {
            mCallBack.currentSelectedLocation(locationId);
        }
    }

    private void sendResultToFragment(int REQUEST_CODE, String message) {
        Intent intent = new Intent();
        intent.putExtra(DialogInterfaces.BUNDLE_DIALOG_MESSAGE, message);
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), REQUEST_CODE, intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * Alert Dialog Callbacks
         */
       /* if (requestCode == DialogInterfaces.DIALOG_REQUESTED_CODE) {
            String message = data.getStringExtra(DialogInterfaces.BUNDLE_DIALOG_MESSAGE);
        }*/
    }
}
