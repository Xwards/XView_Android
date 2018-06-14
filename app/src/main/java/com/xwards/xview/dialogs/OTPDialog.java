package com.xwards.xview.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.xwards.xview.R;

/**
 * Created by nithinjith.pp on 07-03-2018.
 */

public class OTPDialog extends DialogFragment {

    private EditText mEtOTPValue;
    private Spinner mOTpSpinner;
    private OTPCallBack mCallBack;
    private TextView mTvOtpOk;
    private TextView mTvOtpCancel;
    private String mSelectedItem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_otp_dialog, container);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog);
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height_otp);
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mEtOTPValue = view.findViewById(R.id.et_otp_value);
        mTvOtpOk = view.findViewById(R.id.tv_otp_dialog_ok);
        mTvOtpCancel = view.findViewById(R.id.tv_otp_dialog_cancel);
        mOTpSpinner = view.findViewById(R.id.spinner_device_type);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.device_type_spinner_array));
        mOTpSpinner.setAdapter(adapter);
        mOTpSpinner.setSelection(1);
        mSelectedItem = (String) mOTpSpinner.getSelectedItem();
        mOTpSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedItem = (String) parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mTvOtpCancel.setOnClickListener(view12 -> dismiss());
        mTvOtpOk.setOnClickListener(view1 -> {
            if (mCallBack != null) {
                if (mEtOTPValue.getText() == null || mEtOTPValue.getText().toString().isEmpty()) {
                    mCallBack.otpErrorResponse(getString(R.string.alert_dialog_invalid_otp_message));
                    return;
                }
                mCallBack.otpUserResponse(mEtOTPValue.getText().toString(), mSelectedItem);
            }
            dismiss();
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBack = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallBack = (OTPCallBack) context;
    }
}
