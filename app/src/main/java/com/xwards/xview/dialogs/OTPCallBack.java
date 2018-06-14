package com.xwards.xview.dialogs;

/**
 * Created by nithinjith.pp on 07-03-2018.
 */

public interface OTPCallBack {

    /**
     * @param otpMessage- OTP Number
     * @param deviceType  - TV or Cab
     */
    void otpUserResponse(String otpMessage, String deviceType);

    /**
     * Validation
     *
     * @param errorMessage - Message to display the user
     */
    void otpErrorResponse(String errorMessage);
}
