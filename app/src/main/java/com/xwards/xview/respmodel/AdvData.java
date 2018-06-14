package com.xwards.xview.respmodel;

import com.google.gson.annotations.SerializedName;
import com.xwards.xview.base.BaseResponse;

/**
 * Created by nithinjith.pp on 06-11-2017.
 */

public class AdvData extends BaseResponse {

    @SerializedName("Data")
    private AdvCollection AdvCollection;

    public AdvCollection getAdvCollection() {
        return AdvCollection;
    }

    public void setAdvCollection(AdvCollection advCollection) {
        AdvCollection = advCollection;
    }


}
