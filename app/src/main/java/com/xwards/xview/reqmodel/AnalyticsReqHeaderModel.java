package com.xwards.xview.reqmodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nithinjith on 11/21/2017.
 */

public class AnalyticsReqHeaderModel {


    @SerializedName("analytics_data")
    private List<AnalyticsReqModel> AnalyticsList;

    public List<AnalyticsReqModel> getAnalyticsList() {
        return AnalyticsList;
    }

    public void setAnalyticsList(List<AnalyticsReqModel> analyticsList) {
        AnalyticsList = analyticsList;
    }

}
