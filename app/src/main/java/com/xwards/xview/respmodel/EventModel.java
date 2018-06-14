package com.xwards.xview.respmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nithin on 03-03-2018.
 */

public class EventModel implements Parcelable {


    public static final Creator<EventModel> CREATOR = new Creator<EventModel>() {
        @Override
        public EventModel createFromParcel(Parcel in) {
            return new EventModel(in);
        }

        @Override
        public EventModel[] newArray(int size) {
            return new EventModel[size];
        }
    };
    @SerializedName("event_id")
    private Integer EventId;
    @SerializedName("event_header")
    private String EventHeader;
    @SerializedName("event_site_address")
    private String EventUrl;
    @SerializedName("event_logo_url")
    private String EventLogoURl;
    @SerializedName("event_message")
    private String EventMessage;
    @SerializedName("event_start_datetime")
    private String StartDateTime;
    @SerializedName("event_end_datetime")
    private String EndDateTime;
    @SerializedName("venue")
    private String Venue;
    @SerializedName("event_date")
    private String EventDate;
    public EventModel() {

    }
    private EventModel(Parcel in) {
        EventHeader = in.readString();
        EventUrl = in.readString();
        EventLogoURl = in.readString();
        EventMessage = in.readString();
        EventId = in.readInt();
        Venue = in.readString();
        EventDate = in.readString();
    }

    public Integer getEventId() {
        return EventId;
    }

    public void setEventId(Integer eventId) {
        EventId = eventId;
    }

    public String getVenue() {
        return Venue;
    }

    public void setVenue(String venue) {
        Venue = venue;
    }

    public String getEventDate() {
        return EventDate;
    }

    public void setEventDate(String eventDate) {
        EventDate = eventDate;
    }

    public String getEventHeader() {
        return EventHeader;
    }

    public void setEventHeader(String eventHeader) {
        EventHeader = eventHeader;
    }

    public String getEventUrl() {
        return EventUrl;
    }

    public void setEventUrl(String eventUrl) {
        EventUrl = eventUrl;
    }

    public String getEventLogoURl() {
        return EventLogoURl;
    }

    public void setEventLogoURl(String eventLogoURl) {
        EventLogoURl = eventLogoURl;
    }

    public String getEventMessage() {
        return EventMessage;
    }

    public void setEventMessage(String eventMessage) {
        EventMessage = eventMessage;
    }

    public String getStartDateTime() {
        return StartDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        StartDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return EndDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        EndDateTime = endDateTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(EventHeader);
        dest.writeString(EventUrl);
        dest.writeString(EventLogoURl);
        dest.writeString(EventMessage);
        dest.writeString(EventDate);
        dest.writeString(Venue);
        dest.writeInt(EventId);
    }
}
