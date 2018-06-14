package com.xwards.xview.sp;

import android.content.Context;

import com.xwards.xview.enums.DeviceType;

import static com.xwards.xview.sp.SPConstantInterface.DEVICE_TYPE;
import static com.xwards.xview.sp.SPConstantInterface.KISOK_NUMBER;
import static com.xwards.xview.sp.SPConstantInterface.LAST_UPDATED_TIME;
import static com.xwards.xview.sp.SPConstantInterface.SELECTED_LOCATION;
import static com.xwards.xview.sp.SPConstantInterface.SELECTED_ORIENTATION;
import static com.xwards.xview.sp.SPConstantInterface.SESSSION_TOKEN;

/**
 * Nithinjithing Dept.
 * Supply the SP data or insert SP data
 * Concrete class for inserting and retrieving the data
 */
public class SharedPrefDataSupplier extends SPDataHandler {


    public SharedPrefDataSupplier(Context context) {
        super(context);
    }

    public String getSessionToken() {
        return super.getStringSpData(SESSSION_TOKEN, null);
    }

    public void setSessionToken(String sessionToken) {
        super.saveStringToSp(SESSSION_TOKEN, sessionToken);
    }


    public String getLastUpdateTime() {
        return super.getStringSpData(LAST_UPDATED_TIME, null);
    }

    public void setLastUpdatedTime(String sessionToken) {
        super.saveStringToSp(LAST_UPDATED_TIME, sessionToken);
    }

    public int getSelectedLocation() {
        return super.getIntegerSpData(SELECTED_LOCATION, -1);
    }

    public void setSelectedLocation(int selectedLocation) {
        super.setIntegerData(SELECTED_LOCATION, selectedLocation);
    }

    public int getOrientation() {
        return super.getIntegerSpData(SELECTED_ORIENTATION, -1);
    }

    public void setOrientation(int selectedLocation) {
        super.setIntegerData(SELECTED_ORIENTATION, selectedLocation);
    }


    public DeviceType getDeviceType() {
        return DeviceType.valueOf(super.getStringSpData(DEVICE_TYPE, String.valueOf(DeviceType.NONE)));
    }

    public void setDeviceType(DeviceType deviceType) {
        super.saveStringToSp(DEVICE_TYPE, deviceType.toString());
    }

    public String getKisokNumber() {
        return super.getStringSpData(KISOK_NUMBER, "1234");
    }

    public void setKisokNumber(String kisokNumber) {
        super.saveStringToSp(KISOK_NUMBER, kisokNumber);
    }


    public void removeData() {

        super.removeSpData(SPConstantInterface.SELECTED_LOCATION);
        super.removeSpData(SPConstantInterface.SESSSION_TOKEN);
        super.removeSpData(SPConstantInterface.LAST_UPDATED_TIME);
        super.removeSpData(SPConstantInterface.SELECTED_ORIENTATION);
    }


}
