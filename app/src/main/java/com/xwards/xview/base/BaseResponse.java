package com.xwards.xview.base;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nithinjith on 11/6/2017.
 * Base for All response from XWards Server
 * A level of Abstraction
 */

public class BaseResponse implements Parcelable {


    public final static Parcelable.Creator<BaseResponse> CREATOR = new Creator<BaseResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BaseResponse createFromParcel(Parcel in) {
            return new BaseResponse(in);
        }

        public BaseResponse[] newArray(int size) {
            return (new BaseResponse[size]);
        }

    };
    @SerializedName("Message")
    private String Message;
    @SerializedName("Details")
    private String Details;
    @SerializedName("ErrorCode")
    private int ErrorCode;

    public BaseResponse() {
    }

    protected BaseResponse(Parcel in) {
        this.Message = ((String) in.readValue((String.class.getClassLoader())));
        this.Details = ((String) in.readValue((Integer.class.getClassLoader())));
        this.Details = ((String) in.readValue((String.class.getClassLoader())));
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public int getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(int errorCode) {
        ErrorCode = errorCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(Message);
        dest.writeValue(Details);
        dest.writeValue(ErrorCode);

    }
}
