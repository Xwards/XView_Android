package com.xwards.xview.respmodel;

import com.google.gson.annotations.SerializedName;
import com.xwards.xview.base.BaseResponse;

/**
 * Created by Nithin on 04-03-2018.
 */

public class EventRespModel extends BaseResponse {

    @SerializedName("Data")
    private EventRespData EventData;

    public EventRespData getEventData() {
        return EventData;
    }

    public void setEventData(EventRespData eventData) {
        EventData = eventData;
    }

}
