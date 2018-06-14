package com.xwards.xview.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.xwards.xview.R;
import com.xwards.xview.reqmodel.CommentsReqModel;

/**
 * Created by Nithin on 10-03-2018.
 * Dialog to Update the contact US Status
 */

public class ContactUsDialog extends DialogFragment {

    private EditText mEtPhoneNumber;
    private EditText mEtEmail;
    private EditText mEtMessage;
    private ContactUsCallBack mCallBack;
    private TextView mTvOtpOk;
    private TextView mTvOtpCancel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_us_dilaog, container);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.dialog);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        this.mEtPhoneNumber = view.findViewById(R.id.et_contact_us_phone);
        this.mEtEmail = view.findViewById(R.id.et_contact_us_email);
        this.mEtMessage = view.findViewById(R.id.et_contact_us_message);
        this.mTvOtpOk = view.findViewById(R.id.tv_otp_dialog_ok);
        this.mTvOtpCancel = view.findViewById(R.id.tv_otp_dialog_cancel);
        this.mTvOtpCancel.setOnClickListener(view12 -> dismiss());
        this.mTvOtpOk.setOnClickListener(view1 -> {
            if (mCallBack != null) {

                if (!validateFields()) {
                    return;
                }

                CommentsReqModel commentsReqModel = new CommentsReqModel();
                commentsReqModel.setmAdvId("40");
                commentsReqModel.setEmailAddress(mEtEmail.getText().toString());
                commentsReqModel.setPhoneNumber(mEtPhoneNumber.getText().toString());
                commentsReqModel.setComments(mEtMessage.getText().toString());
                mCallBack.updateUserComments(commentsReqModel);
            }
            dismiss();
        });
    }

    private boolean validateFields() {
        if (mEtPhoneNumber.getText() == null) {
            mCallBack.updateUserCmntsErrorStatus(getString(R.string.alert_dialog_ivalid_phone));
            return false;
        }
        if (!isValidMobile(mEtPhoneNumber.getText().toString())) {
            mCallBack.updateUserCmntsErrorStatus(getString(R.string.alert_dialog_ivalid_phone));
            return false;
        }
        return true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBack = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallBack = (ContactUsCallBack) context;
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}
