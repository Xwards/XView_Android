package com.xwards.xview.adapters;

import android.view.View;

/**
 * Created by Nithinjith on 28-12-2016.
 * This is the callback to get item click from an Adapter
 */

public interface ClickListener {

    /**
     * CallBack to get the adapter
     *
     * @param view-Clicked View
     * @param position     - Current Adapter Position
     */
    void onClick(View view, int position);
}
