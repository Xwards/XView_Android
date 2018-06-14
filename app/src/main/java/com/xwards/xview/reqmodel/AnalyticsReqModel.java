package com.xwards.xview.reqmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nithinjith on 11/19/2017.
 */

public class AnalyticsReqModel implements Parcelable {

    public static final Parcelable.Creator<AnalyticsReqModel> CREATOR = new Parcelable.Creator<AnalyticsReqModel>() {
        @Override
        public AnalyticsReqModel createFromParcel(Parcel in) {
            return new AnalyticsReqModel(in);
        }

        @Override
        public AnalyticsReqModel[] newArray(int size) {
            return new AnalyticsReqModel[size];
        }
    };
    @SerializedName("adv_id")
    @Expose
    private Integer AdvId;
    @SerializedName("adv_play_count")
    @Expose
    private Integer AdvPlayCount = 0;
    @SerializedName("adv_touch_count")
    @Expose
    private Integer AdvTouchCount = 0;
    @SerializedName("adv_active_time")
    @Expose
    private String AdvActiveTime;

    protected AnalyticsReqModel(Parcel in) {
        if (in.readByte() == 0) {
            AdvId = null;
        } else {
            AdvId = in.readInt();
        }
        if (in.readByte() == 0) {
            AdvPlayCount = null;
        } else {
            AdvPlayCount = in.readInt();
        }
        if (in.readByte() == 0) {
            AdvTouchCount = null;
        } else {
            AdvTouchCount = in.readInt();
        }
        AdvActiveTime = in.readString();

    }

    public AnalyticsReqModel() {
    }

    public Integer getAdvId() {
        return AdvId;
    }

    public void setAdvId(Integer advId) {
        AdvId = advId;
    }

    public Integer getAdvPlayCount() {
        return AdvPlayCount;
    }

    public void setAdvPlayCount(Integer advPlayCount) {
        AdvPlayCount = advPlayCount;
    }

    public Integer getAdvTouchCount() {
        return AdvTouchCount;
    }

    public void setAdvTouchCount(Integer advTouchCount) {
        AdvTouchCount = advTouchCount;
    }

    public String getAdvActiveTime() {
        return AdvActiveTime;
    }

    public void setAdvActiveTime(String advActiveTime) {
        AdvActiveTime = advActiveTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (AdvId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(AdvId);
        }
        if (AdvPlayCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(AdvPlayCount);
        }
        if (AdvTouchCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(AdvTouchCount);
        }
        dest.writeString(AdvActiveTime);

    }

}
