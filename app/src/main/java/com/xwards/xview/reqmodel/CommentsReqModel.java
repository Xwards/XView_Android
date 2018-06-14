package com.xwards.xview.reqmodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SAMSUNG on 25-02-2018.
 */

public class CommentsReqModel {

    @SerializedName("comment")
    private String Comments;
    @SerializedName("phone_number")
    private String PhoneNumber;
    @SerializedName("email")
    private String EmailAddress;
    @SerializedName("adv_id")
    private String mAdvId;

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getmAdvId() {
        return mAdvId;
    }

    public void setmAdvId(String mAdvId) {
        this.mAdvId = mAdvId;
    }
}
