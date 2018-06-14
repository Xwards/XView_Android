package com.xwards.xview.reqmodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nithinjith on 11/6/2017.
 */

public class AdvReqModel {

    @SerializedName("location_id")
    private int location_id;
    @SerializedName("device_id")
    private String device_id;

    public int getLocationId() {
        return location_id;
    }

    public void setLocationId(int locationId) {
        location_id = locationId;
    }

    public String getDeviceId() {
        return device_id;
    }

    public void setDeviceId(String deviceId) {
        device_id = deviceId;
    }


}
