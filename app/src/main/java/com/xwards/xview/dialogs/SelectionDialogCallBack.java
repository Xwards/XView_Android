package com.xwards.xview.dialogs;

import com.xwards.xview.splash.LocationModel;

/**
 * Created by Nithinjith on 11/5/2017.
 */

public interface SelectionDialogCallBack {
    /**
     * Current Device Location
     *
     * @param location
     */
    void currentSelectedLocation(LocationModel location);
}
