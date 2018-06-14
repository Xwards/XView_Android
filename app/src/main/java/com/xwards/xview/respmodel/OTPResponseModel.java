package com.xwards.xview.respmodel;

import com.google.gson.annotations.SerializedName;
import com.xwards.xview.base.BaseResponse;

/**
 * Created by Nithinjith on 24-02-2018.
 */

public class OTPResponseModel extends BaseResponse {

    @SerializedName("Data")
    private OTPData mOTPData;

    public OTPData getOTPData() {
        return mOTPData;
    }

    public void setOTPData(OTPData OTPData) {
        this.mOTPData = OTPData;
    }

}
