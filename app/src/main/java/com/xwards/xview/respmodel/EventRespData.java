package com.xwards.xview.respmodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nithin on 04-03-2018.
 */

public class EventRespData {

    @SerializedName("eventsList")
    private List<EventModel> EventList;

    public List<EventModel> getEventList() {
        return EventList;
    }

    public void setEventList(List<EventModel> eventList) {
        EventList = eventList;
    }
}
