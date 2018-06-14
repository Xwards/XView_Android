package com.xwards.xview.dialogs;

import com.xwards.xview.respmodel.EventModel;

/**
 * Created by Nithin on 10-03-2018.
 */

public interface EventDialogCallBack {

    String EVENT_DIALOG_OBJ_KEY = "current_event_dialog";

    /**
     * Update the Event Data to the Activity
     *
     * @param object
     */
    void updateEventObject(EventModel object);
}
