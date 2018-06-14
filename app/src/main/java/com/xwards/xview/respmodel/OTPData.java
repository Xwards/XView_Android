package com.xwards.xview.respmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nithinjith on 24-02-2018.
 */

public class OTPData {


    @SerializedName("session_key")
    @Expose
    private String mSessionKey;
    @SerializedName("admin_name")
    @Expose
    private String mAdminName;
    @SerializedName("admin_id")
    @Expose
    private String mAdminId;
    @SerializedName("is_super_admin")
    @Expose
    private boolean isSuperAdmin;
    @SerializedName("device_id")
    @Expose
    private String mDeviceId;
    @SerializedName("device_type")
    @Expose
    private String DeviceType;

    public String getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(String deviceType) {
        DeviceType = deviceType;
    }

    public String getSessionKey() {
        return mSessionKey;
    }

    public void setSessionKey(String sessionKey) {
        mSessionKey = sessionKey;
    }

    public String getAdminName() {
        return mAdminName;
    }

    public void setAdminName(String adminName) {
        mAdminName = adminName;
    }

    public String getAdminId() {
        return mAdminId;
    }

    public void setAdminId(String adminId) {
        mAdminId = adminId;
    }

    public boolean isSuperAdmin() {
        return isSuperAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        isSuperAdmin = superAdmin;
    }

    public String getDeviceId() {
        return mDeviceId;
    }

    public void setDeviceId(String deviceId) {
        mDeviceId = deviceId;
    }


}
