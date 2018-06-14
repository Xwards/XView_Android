package com.xwards.xview.home;

import com.xwards.xview.respmodel.AdVideoModel;

/**
 * Created by nithinjith.pp on 08-11-2017.
 */

public interface PagerCallBack {

    String PAGER_SINGLE_ADVERTISEMENT = "Pager_single_advertisement_data";

    /**
     * To Notify the Video Completed Status to Main
     *
     * @param adVideoModel - Update the current Running Item
     */
    void videoCompletedCallBack(AdVideoModel adVideoModel);

    /**
     * Update the Item Click
     *
     * @param adVideoModel - Current Selected Item
     */
    void videoItemClickCallBack(AdVideoModel adVideoModel);

    void updatePlayCount(AdVideoModel mAdvObject);
}
