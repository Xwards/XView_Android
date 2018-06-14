package com.xwards.xview.respmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SAMSUNG on 24-02-2018.
 */

public class CampaignRespData {

    @SerializedName("campaign_id")
    @Expose
    private String CampaignId;

    @SerializedName("campaign_name")
    @Expose
    private String CampaignName;

    @SerializedName("campaign_type")
    @Expose
    private String CampaignType;
    @SerializedName("campaign_list")
    @Expose
    private List<CampaignModel> CampaignList;

    public String getCampaignId() {
        return CampaignId;
    }

    public void setCampaignId(String campaignId) {
        CampaignId = campaignId;
    }

    public String getCampaignName() {
        return CampaignName;
    }

    public void setCampaignName(String campaignName) {
        CampaignName = campaignName;
    }

    public String getCampaignType() {
        return CampaignType;
    }

    public void setCampaignType(String campaignType) {
        CampaignType = campaignType;
    }

    public List<CampaignModel> getCampaignList() {
        return CampaignList;
    }

    public void setCampaignList(List<CampaignModel> campaignList) {
        CampaignList = campaignList;
    }

}
