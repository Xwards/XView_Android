package com.xwards.xview.rest;

import com.xwards.xview.base.BaseResponse;
import com.xwards.xview.reqmodel.AnalyticsReqHeaderModel;
import com.xwards.xview.reqmodel.CommentsReqModel;
import com.xwards.xview.reqmodel.OTPReqModel;
import com.xwards.xview.respmodel.AdvData;
import com.xwards.xview.respmodel.AnalyticsRespModel;
import com.xwards.xview.respmodel.CampaignRespModel;
import com.xwards.xview.respmodel.EventRespModel;
import com.xwards.xview.respmodel.OTPResponseModel;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.xwards.xview.rest.APIConstants.GET_ADVERTISEMENT;
import static com.xwards.xview.rest.APIConstants.GET_CAMPAIGN;
import static com.xwards.xview.rest.APIConstants.GET_EVENT_LIST;
import static com.xwards.xview.rest.APIConstants.SEND_OTP;
import static com.xwards.xview.rest.APIConstants.UPDATE_ANALYTICS;
import static com.xwards.xview.rest.APIConstants.UPDATE_USER_COMMENTS;

/**
 * Created by nithinjith.pp on 31-10-2017.
 * AN API interface using REST- RETROFIT AND OBSERVER
 */
public interface APIRepository {
    /**
     * Get Advertisement is the Second Important API in this Application
     * This API will help to get the Advertisement from the Server
     */
    @GET(GET_ADVERTISEMENT)
    Observable<AdvData> getAdvertisement(@Header("session_key") String sessionToken,
                                         @Query("ads_type") String adType,
                                         @Query("last_ping_dateTime") String dateTime);

    /**
     * Get Event List
     * The Event List will get the available events for the Super Admin
     */
    @GET(GET_EVENT_LIST)
    Observable<EventRespModel> getEventList(@Header("session_key")
                                                    String sessionToken,
                                            @Query("last_ping_dateTime") String dateTime);

    /**
     * To update the Analytics Status of the device
     */
    @POST(UPDATE_ANALYTICS)
    Observable<AnalyticsRespModel> updateAnalytics(@Header("session_key") String sessionToken,
                                                   @Body AnalyticsReqHeaderModel modelList);

    @POST(GET_CAMPAIGN)
    Observable<CampaignRespModel> getCampaign(@Header("session_key") String sessionToken);

    /**
     * The Important API in the Application
     * The API will authenticate the device and
     * If the user success the Authentication- The API will give a session token
     * All the API by default header will be this session token
     * The admin analyse the Presence & Uniques of this device through session Token
     */
    @POST(SEND_OTP)
    Observable<OTPResponseModel> getOptData(@Body OTPReqModel reqModel);

    /**
     * Update user comments
     * This will update the user comments in cab devices
     * The contact us will fire this API
     */
    @POST(UPDATE_USER_COMMENTS)
    Observable<BaseResponse> updateUserComments(@Header("session_key") String sessionToken,
                                                @Body CommentsReqModel requestModel);

}
