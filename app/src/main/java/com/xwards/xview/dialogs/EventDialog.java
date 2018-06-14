package com.xwards.xview.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xwards.xview.R;
import com.xwards.xview.respmodel.EventModel;

/**
 * Created by Nithin on 10-03-2018.
 * EVENT DATA POPUP
 * If the user clicks on an event- That data will Popup the Event Dialog
 */

public class EventDialog extends DialogFragment implements View.OnClickListener {

    public ImageView mIvLogo;
    public TextView mTvHeader;
    private TextView mTvMessage;
    private TextView mTvUrl;
    private EventModel mEventObject;
    private ImageView mIvClose;
    private EventDialogCallBack mCallBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_dialog, container);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.dialog);
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        this.mIvLogo = view.findViewById(R.id.iv_event_logo);
        this.mTvHeader = view.findViewById(R.id.tv_event_header);
        this.mTvMessage = view.findViewById(R.id.tv_event_message);
        this.mTvUrl = view.findViewById(R.id.tv_event_url);
        this.mIvClose = view.findViewById(R.id.iv_close_btn);
        this.mIvClose.setOnClickListener(this);
        this.mTvUrl.setOnClickListener(this);
        Bundle data = getArguments();

        if (data != null && data.containsKey(EventDialogCallBack.EVENT_DIALOG_OBJ_KEY)) {
            mEventObject = data.getParcelable(EventDialogCallBack.EVENT_DIALOG_OBJ_KEY);
        }
        if (mEventObject != null) {
            updateDataToUI();
        }
    }

    private void updateDataToUI() {
        if (nullChecker(mEventObject.getEventHeader())) {
            mTvHeader.setText(mEventObject.getEventHeader());
        }
        if (nullChecker(mEventObject.getEventMessage())) {
            mTvMessage.setText(mEventObject.getEventMessage());
        }

        if (nullChecker(mEventObject.getEventUrl())) {
            mTvUrl.setText(mEventObject.getEventUrl());
        }
        if (nullChecker(mEventObject.getEventLogoURl())) {
            Glide.with(getActivity()).
                    load(mEventObject.getEventLogoURl()).
                    into(mIvLogo);
        }
    }

    private boolean nullChecker(String key) {

        return key != null && key.length() > 0;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBack = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallBack = (EventDialogCallBack) context;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close_btn: {
                dismiss();
            }
            break;
            case R.id.tv_event_url: {
                if (mCallBack != null && mEventObject != null) {
                    mCallBack.updateEventObject(mEventObject);
                }
            }
            break;
        }
    }
}
