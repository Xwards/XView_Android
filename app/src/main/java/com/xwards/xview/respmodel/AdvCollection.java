package com.xwards.xview.respmodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nithinjith.pp on 06-11-2017.
 */

public class AdvCollection {
    @SerializedName("SessionToken")
    private String SessionToken;
    @SerializedName("videoAds")
    private List<AdVideoModel> AdVideoList;
    @SerializedName("updatedAdsIds")
   /* private List<Integer> UpdatedAdvIdList;
    @SerializedName("activeAdsIds")*/
    private List<Integer> ActiveAdvIdList;
    @SerializedName("deviceType")
    private String DeviceType;
    @SerializedName("otpNumber")
    private String KisokExitNumber;

    public String getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(String deviceType) {
        DeviceType = deviceType;
    }

    public String getKisokExitNumber() {
        return KisokExitNumber;
    }

    public void setKisokExitNumber(String kisokExitNumber) {
        KisokExitNumber = kisokExitNumber;
    }

    public String getSessionToken() {
        return SessionToken;
    }

    public void setSessionToken(String sessionToken) {
        SessionToken = sessionToken;
    }

    public List<AdVideoModel> getAdVideoList() {
        return AdVideoList;
    }

    public void setAdVideoList(List<AdVideoModel> adVideoList) {
        AdVideoList = adVideoList;
    }

  /*  public List<Integer> getUpdatedAdvIdList() {
        return UpdatedAdvIdList;
    }

    public void setUpdatedAdvIdList(List<Integer> updatedAdvIdList) {
        UpdatedAdvIdList = updatedAdvIdList;
    }*/

    public List<Integer> getActiveAdvIdList() {
        return ActiveAdvIdList;
    }

    public void setActiveAdvIdList(List<Integer> activeAdvIdList) {
        ActiveAdvIdList = activeAdvIdList;
    }

}
