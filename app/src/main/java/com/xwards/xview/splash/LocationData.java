package com.xwards.xview.splash;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.xwards.xview.base.BaseResponse;

/**
 * Created by nithinjith.pp on 06-11-2017.
 */

public class LocationData extends BaseResponse implements Parcelable {

    public final static Parcelable.Creator<LocationData> CREATOR = new Creator<LocationData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public LocationData createFromParcel(Parcel in) {
            return new LocationData(in);
        }

        public LocationData[] newArray(int size) {
            return (new LocationData[size]);
        }

    };
    @SerializedName("Data")
    private LocationCollection LocationResp;

    protected LocationData(Parcel in) {
        this.LocationResp = (LocationCollection) in.readValue((String.class.getClassLoader()));
    }

    public LocationCollection getLocationResp() {
        return LocationResp;
    }

    public void setLocationResp(LocationCollection locationResp) {
        LocationResp = locationResp;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(LocationResp);
    }


}
