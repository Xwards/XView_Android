package com.xwards.xview.splash;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nithinjith on 11/6/2017.
 */

public class LocationModel implements Parcelable {

    public final static Parcelable.Creator<LocationModel> CREATOR = new Creator<LocationModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public LocationModel createFromParcel(Parcel in) {
            return new LocationModel(in);
        }

        public LocationModel[] newArray(int size) {
            return (new LocationModel[size]);
        }

    };
    @SerializedName("id")
    private int mLocationId;
    @SerializedName("location_name")
    private String mLocationName;
    @SerializedName("mobile_orientation")
    private String mOrientation;

    protected LocationModel(Parcel in) {
        this.mLocationName = (String) in.readValue((String.class.getClassLoader()));
        this.mLocationId = (int) in.readValue((String.class.getClassLoader()));
    }

    public int getLocationId() {
        return mLocationId;
    }

    public void setLocationId(int locationId) {
        mLocationId = locationId;
    }

    public String getLocationName() {
        return mLocationName;
    }

    public void setLocationName(String locationName) {
        mLocationName = locationName;
    }

    public String getOrientation() {
        return mOrientation;
    }

    public void setOrientation(String orientation) {
        mOrientation = orientation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(mLocationName);
        parcel.writeValue(mLocationId);
    }

}
