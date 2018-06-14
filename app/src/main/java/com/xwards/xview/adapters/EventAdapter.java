package com.xwards.xview.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xwards.xview.R;
import com.xwards.xview.respmodel.EventModel;

import java.util.List;

/**
 * Created by Nithin on 03-03-2018.
 * The Server Generated Events will be adapted with the help of this recycler View
 * The Data will be user specific and Pin to the Session Key
 */

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final Context mContext;
    private final LayoutInflater mInflater;
    private final List<EventModel> mEventList;
    private ClickListener mClickListener;

    public EventAdapter(Context context, List<EventModel> eventList) {
        this.mContext = context;
        this.mEventList = eventList;
        this.mInflater = LayoutInflater.from(context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.event_single_item_layout, parent, false);
        return new EventHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        EventHolder eventHolder = (EventHolder) holder;

        if (nullChecker(mEventList.get(position).getEventHeader())) {
            eventHolder.mTvHeader.setText(mEventList.get(position).getEventHeader());
        }

        if (nullChecker(mEventList.get(position).getVenue())) {
            eventHolder.mTvMessage.setText(mEventList.get(position).getVenue());
        }

        if (nullChecker(mEventList.get(position).getEventDate())) {
            eventHolder.mTvUrl.setText(mEventList.get(position).getEventDate());
        }
        if (nullChecker(mEventList.get(position).getEventLogoURl())) {
            Glide.with(mContext).
                    load(mEventList.get(position).getEventLogoURl()).
                    into(eventHolder.mIvLogo);
        }
    }


    private boolean nullChecker(String key) {

        return key != null && key.length() > 0;
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    public void setClickListener(ClickListener listener) {

        this.mClickListener = listener;

    }

    public class EventHolder extends RecyclerView.ViewHolder {

        public ImageView mIvLogo;
        public TextView mTvHeader;
        TextView mTvMessage;
        TextView mTvUrl;
        RelativeLayout mRlRoot;

        public EventHolder(View itemView) {
            super(itemView);
            mIvLogo = itemView.findViewById(R.id.iv_event_logo);
            mTvHeader = itemView.findViewById(R.id.tv_event_header);
            mTvMessage = itemView.findViewById(R.id.tv_event_message);
            mTvUrl = itemView.findViewById(R.id.tv_event_url);
            mRlRoot = itemView.findViewById(R.id.rl_event_root);
            mRlRoot.setOnClickListener(v -> {
                if (mClickListener != null) {
                    mClickListener.onClick(v, getLayoutPosition());
                }
            });
        }
    }
}
