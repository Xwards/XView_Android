package com.xwards.xview.dialogs;

import com.xwards.xview.reqmodel.CommentsReqModel;

/**
 * Created by Nithin on 10-03-2018.
 */

public interface ContactUsCallBack {

    /**
     * To Update the Comments of the User- If the user enter some comments
     * The Dialog will notify it to the MainActivity class
     *
     * @param model
     */
    void updateUserComments(CommentsReqModel model);

    /**
     * Validation Process
     *
     * @param status
     */
    void updateUserCmntsErrorStatus(String status);
}
