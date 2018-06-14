package com.xwards.xview.respmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SAMSUNG on 24-02-2018.
 */

public class CampaignModel {

    @SerializedName("adv_id")
    @Expose
    private String VideoAdId;
    @SerializedName("adb_id")
    @Expose
    private String BannerAdId;

    public String getVideoAdId() {
        return VideoAdId;
    }

    public void setVideoAdId(String videoAdId) {
        VideoAdId = videoAdId;
    }

    public String getBannerAdId() {
        return BannerAdId;
    }

    public void setBannerAdId(String bannerAdId) {
        BannerAdId = bannerAdId;
    }


}
