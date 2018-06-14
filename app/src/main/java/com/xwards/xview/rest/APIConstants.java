package com.xwards.xview.rest;

/**
 * Created by nithinjith.pp on 31-10-2017.
 * The REST API End Points
 * Basically this is Separating the Base URL to each individual Points
 */

public interface APIConstants {

    /**
     * To Fetch the User location - This is an OLD API not using the second Phase
     */
    String GET_USER_LOCATION = "getLocations";

    /**
     * Get Advertisement is the Second Important API in this Application
     * This API will help to get the Advertisement from the Server
     */
    String GET_ADVERTISEMENT = "getAdvertisementList";

    /**
     * Getting the AD Update
     * Lose his priority in the Second Phase of Dev
     * We are fetching the update via last updated date logic
     */
    String CHECK_UPDATES = "checkAdsUpdates";
    /**
     * To update the Analytics Status of the device
     */
    String UPDATE_ANALYTICS = "sendAnalytics";
    /**
     * Campaign sequence are fetching through Ad List
     */
    String GET_CAMPAIGN = "getCampaign";

    /**
     * The Important API in the Application
     * The API will authenticate the device and
     * If the user success the Authentication- The API will give a session token
     * All the API by default header will be this session token
     * The admin analyse the Presence & Uniques of this device through session Token
     */
    String SEND_OTP = "sendOTP";

    /**
     * Update user comments
     * This will update the user comments in cab devices
     * The contact us will fire this API
     */
    String UPDATE_USER_COMMENTS = "update_user_comments";

    /**
     * Get Event List
     * The Event List will get the available events for the Super Admin
     */
    String GET_EVENT_LIST = "getEventsList";
}
