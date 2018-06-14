package com.xwards.xview.reqmodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nithinjith on 24-02-2018.
 */

public class OTPReqModel {


    @SerializedName("otp_number")
    private String OTPNumber;
    @SerializedName("device_imei")
    private String IMEI;
    @SerializedName("device_mac_address")
    private String MacAddress;
    @SerializedName("current_latitude")
    private String Latitude;
    @SerializedName("current_longitude")
    private String Longitude;
    @SerializedName("device_type")
    private String DeviceType;


    public String getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(String deviceType) {
        DeviceType = deviceType;
    }


    public String getOTPNumber() {
        return OTPNumber;
    }

    public void setOTPNumber(String OTPNumber) {
        this.OTPNumber = OTPNumber;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getMacAddress() {
        return MacAddress;
    }

    public void setMacAddress(String macAddress) {
        MacAddress = macAddress;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }
}
