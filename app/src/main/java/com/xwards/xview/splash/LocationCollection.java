package com.xwards.xview.splash;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nithinjith.pp on 06-11-2017.
 */

public class LocationCollection implements Parcelable {

    public final static Parcelable.Creator<LocationCollection> CREATOR = new Creator<LocationCollection>() {


        @SuppressWarnings({
                "unchecked"
        })
        public LocationCollection createFromParcel(Parcel in) {
            return new LocationCollection(in);
        }

        public LocationCollection[] newArray(int size) {
            return (new LocationCollection[size]);
        }

    };
    @SerializedName("locationList")
    private List<LocationModel> LocationList;

    protected LocationCollection(Parcel in) {
        this.LocationList = (List<LocationModel>) in.readValue((String.class.getClassLoader()));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(LocationList);
    }

    public List<LocationModel> getLocationList() {
        return LocationList;
    }

    public void setLocationList(List<LocationModel> locationList) {
        LocationList = locationList;
    }
}
