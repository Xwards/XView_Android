package com.xwards.xview.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.xwards.xview.R;


/**
 * Created by Nithinjith on 02-08-2017.
 */

public class GeneralDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String message = getArguments().getString(DialogInterfaces.BUNDLE_DIALOG_MESSAGE);
        final boolean isCancelRequired = getArguments().getBoolean(DialogInterfaces.BUNDLE_DIALOG_CANCEL_REQUIRED);
        final boolean isFromFragment = getArguments().getBoolean(DialogInterfaces.BUNDLE_DIALOG_IS_FROM_FRAGMENT);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle(getActivity().getString(R.string.app_name));
        dialog.setMessage(message);
        dialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (isFromFragment) {
                    sendResultToFragment(DialogInterfaces.DIALOG_REQUESTED_CODE, message);
                } else {
                    sendResultToActivity(message);
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

    private void sendResultToActivity(String message) {
        DialogInterfaces mCallBack = (DialogInterfaces) getActivity();
        if (mCallBack != null) {
            mCallBack.updateDialogResult(message);
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
