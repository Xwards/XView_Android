package com.xwards.xview.dialogs;

/**
 * Created by Nithinjith on 29-06-2017.
 */

public interface DialogInterfaces {

    /**
     * Common dialog request code
     */
    int DIALOG_REQUESTED_CODE = 10001;

    /**
     * Date Picker Dialog Request Code
     */

    int DIALOG_REQUEST_DATE_PICKER = 10002;

    /**
     * Message Key to separate from Bundle
     */
    String BUNDLE_DIALOG_MESSAGE = "alert_dialog_message_key";

    /**
     * Bundle Key to check whether the cancel is required or not
     */
    String BUNDLE_DIALOG_CANCEL_REQUIRED = "is_cancel_dialog_required";

    /**
     * Check whether the dialog loaded from activity or fragment
     */
    String BUNDLE_DIALOG_IS_FROM_FRAGMENT = "is_the_dialog_loaded_from_fragment";
    /**
     * Location Object
     */
    String BUNDLE_DIALOG_LOCATION_DATA = "alert_dialog_location_object";

    /**
     * @param message - Interface Callback to Update the Dialog result.
     */
    void updateDialogResult(String message);

}
