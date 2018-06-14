package com.xwards.xview.respmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xwards.xview.base.BaseResponse;

/**
 * Created by SAMSUNG on 24-02-2018.
 */

public class CampaignRespModel extends BaseResponse {

    @SerializedName("Data")
    @Expose
    private CampaignRespData Data;

    public CampaignRespData getData() {
        return Data;
    }

    public void setData(CampaignRespData data) {
        Data = data;
    }


}
