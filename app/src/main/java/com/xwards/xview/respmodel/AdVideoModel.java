package com.xwards.xview.respmodel;

/**
 * Created by nithinjith.pp on 24-10-2017.
 */


import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xwards.xview.base.BaseResponse;


public class AdVideoModel extends BaseResponse implements Parcelable, Comparable<Integer> {

    public final static Parcelable.Creator<AdVideoModel> CREATOR = new Creator<AdVideoModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public AdVideoModel createFromParcel(Parcel in) {
            return new AdVideoModel(in);
        }

        public AdVideoModel[] newArray(int size) {
            return (new AdVideoModel[size]);
        }

    };
    @SerializedName("adv_name")
    @Expose
    private String advName;
    @PrimaryKey()
    @SerializedName("adv_id")
    @Expose
    private Integer advId;
    @SerializedName("adv_preview_url")
    @Expose
    private String advPreviewUrl;
    @SerializedName("adv_url")
    @Expose
    private String advUrl;
    @SerializedName("adv_start_date")
    @Expose
    private String advStartDate;

    @SerializedName("adv_end_date")
    @Expose
    private String advEndDate;
    private String advDbWriteSatus;
    private long PlayDuration = 0;
    @SerializedName("adv_local_path")
    @Expose
    private String advLocalPath;

    @SerializedName("video_website_url")
    @Expose
    private String AdvSiteURL;
    @SerializedName("banner_website_url")
    @Expose
    private String BannerSiteURL;
    @SerializedName("adv_banner_url")
    @Expose
    private String BannerUrl;

    protected AdVideoModel(Parcel in) {
        this.advName = ((String) in.readValue((String.class.getClassLoader())));
        this.advId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.advPreviewUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.advUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.advStartDate = ((String) in.readValue((String.class.getClassLoader())));
        this.advEndDate = ((String) in.readValue((String.class.getClassLoader())));
        this.advLocalPath = ((String) in.readValue((String.class.getClassLoader())));
        this.BannerUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.AdvSiteURL = ((String) in.readValue((String.class.getClassLoader())));
        this.BannerSiteURL = ((String) in.readValue((String.class.getClassLoader())));
    }


    public AdVideoModel() {

    }

    public String getBannerSiteURL() {
        return BannerSiteURL;
    }

    public void setBannerSiteURL(String bannerSiteURL) {
        BannerSiteURL = bannerSiteURL;
    }

    public String getBannerUrl() {
        return BannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        BannerUrl = bannerUrl;
    }

    public String getAdvDbWriteSatus() {
        return advDbWriteSatus;
    }

    public void setAdvDbWriteSatus(String advDbWriteSatus) {
        this.advDbWriteSatus = advDbWriteSatus;
    }

    public long getPlayDuration() {
        return PlayDuration;
    }

    public void setPlayDuration(long playDuration) {
        PlayDuration = playDuration;
    }

    public String getAdvLocalPath() {
        return advLocalPath;
    }

    public void setAdvLocalPath(String advLocalPath) {
        this.advLocalPath = advLocalPath;
    }

    public String getAdvName() {
        return advName;
    }

    public void setAdvName(String advName) {
        this.advName = advName;
    }

    public Integer getAdvId() {
        return advId;
    }

    public void setAdvId(Integer advId) {
        this.advId = advId;
    }

    public String getAdvPreviewUrl() {
        return advPreviewUrl;
    }

    public void setAdvPreviewUrl(String advPreviewUrl) {
        this.advPreviewUrl = advPreviewUrl;
    }

    public String getAdvUrl() {
        return advUrl;
    }

    public void setAdvUrl(String advUrl) {
        this.advUrl = advUrl;
    }

    public String getAdvStartDate() {
        return advStartDate;
    }

    public void setAdvStartDate(String advStartDate) {
        this.advStartDate = advStartDate;
    }

    public String getAdvEndDate() {
        return advEndDate;
    }

    public void setAdvEndDate(String advEndDate) {
        this.advEndDate = advEndDate;
    }

    public String getAdvSiteURL() {
        return AdvSiteURL;
    }

    public void setAdvSiteURL(String advSiteURL) {
        AdvSiteURL = advSiteURL;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(advName);
        dest.writeValue(advId);
        dest.writeValue(advPreviewUrl);
        dest.writeValue(advUrl);
        dest.writeValue(advStartDate);
        dest.writeValue(advEndDate);
        dest.writeValue(advLocalPath);
        dest.writeValue(AdvSiteURL);
        dest.writeValue(BannerUrl);
        dest.writeValue(BannerSiteURL);


    }

    public int describeContents() {
        return 0;
    }


    @Override
    public int compareTo(@NonNull Integer integer) {
        return (this.advId == integer) ? 0 : 1;
    }
}
