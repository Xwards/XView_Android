package com.xwards.xview.splash;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.xwards.xview.R;
import com.xwards.xview.base.BaseActivity;
import com.xwards.xview.dialogs.DialogInterfaces;
import com.xwards.xview.dialogs.OTPCallBack;
import com.xwards.xview.dialogs.OTPDialog;
import com.xwards.xview.dialogs.SelectionDialogCallBack;
import com.xwards.xview.enums.DeviceType;
import com.xwards.xview.home.MainActivity;
import com.xwards.xview.reqmodel.OTPReqModel;
import com.xwards.xview.respmodel.OTPResponseModel;
import com.xwards.xview.rest.APIRepository;
import com.xwards.xview.rest.NetworkCallBack;
import com.xwards.xview.rest.NetworkClient;
import com.xwards.xview.sp.SharedPrefDataSupplier;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends BaseActivity implements DialogInterfaces,
        SelectionDialogCallBack, OTPCallBack {

    private static final long SPLASH_TIME_OUT = 2000;
    private FusedLocationProviderClient mFusedLocationClient;
    private SharedPrefDataSupplier mSupplier;
    private String TAG = "SplashActivity";
    private String mLatitude = "9.93606";
    private String mLongitude = "76.2614";


    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null &&
                permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void checkBrightnessStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                // Do stuff here
                showOtpDialog();
            } else {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else {
            showOtpDialog();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Answers(), new Crashlytics());
        this.mSupplier = new SharedPrefDataSupplier(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mCompositeDisposable = new CompositeDisposable();
        mApiRepository = NetworkClient.getRetrofit().create(APIRepository.class);

    }

    private void permissionHandler() {
        if (Build.VERSION.SDK_INT >= 23) {
            int PERMISSION_ALL = 1;
            String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            if (!hasPermissions(SplashActivity.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(SplashActivity.this, PERMISSIONS, PERMISSION_ALL);
            } else {
                checkSessionStatus();
            }
        } else {
            checkSessionStatus();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(() -> permissionHandler(), SPLASH_TIME_OUT);
    }

    private void checkSessionStatus() {
        if (mSupplier.getSessionToken() != null) {
            startMainActivity();
        } else {
//            getLastLocation();
           checkBrightnessStatus();
        }
    }

    private void showOtpDialog() {
        FragmentManager manager = getSupportFragmentManager();
        OTPDialog dialog = new OTPDialog();
        dialog.show(manager, "OTP");
    }

    private void processOTPRequest(String otp, String deviceType) {
        OTPReqModel reqModel = new OTPReqModel();
        reqModel.setOTPNumber(otp.trim());
        reqModel.setIMEI(getDeviceIMEI());
        reqModel.setMacAddress(getMacAddress());
        reqModel.setLatitude(mLatitude);
        reqModel.setLongitude(mLongitude);
        reqModel.setDeviceType(deviceType);
        sendOtp(reqModel);
    }

    private void startMainActivity() {
        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    @Override
    public void updateDialogResult(String message) {
        if (message.equalsIgnoreCase(getString(R.string.dialog_message_otp_error))) {
            showOtpDialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            permissionHandler();
        }
    }


    @Override
    public void currentSelectedLocation(LocationModel location) {
        mSupplier.setSelectedLocation(location.getLocationId());
        Bundle bundle = new Bundle();
        if (location.getOrientation() != null && location.getOrientation().length() > 0) {
            mSupplier.setOrientation(Integer.parseInt(location.getOrientation()));
        }
        bundle.putInt(DEVICE_ORIENTATION_KEY, (mSupplier.getOrientation() == -1) ?
                ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE :
                ((mSupplier.getOrientation() == 1) ? ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE :
                        ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT));
    }

    private void sendOtp(OTPReqModel model) {
        if (!super.isNetworkConnectionAvailable(this)) {
            super.displayNetworkErrorDialog();
            return;
        }
        super.showProgressDialog(getString(R.string.dialog_message_auth));
        mCompositeDisposable.add(mApiRepository.getOptData(model).
                observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).
                subscribeWith(new NetworkCallBack<OTPResponseModel>() {
                    @Override
                    public void onComplete() {
                        hideProgressDialog();
                    }

                    @Override
                    public void onSuccess(OTPResponseModel model) {
                        otpSuccessHandler(model);
                        hideProgressDialog();
                    }

                    @Override
                    public void onFailure(String message, String errorCode) {
                        handleOtpError();
                        hideProgressDialog();
                    }

                    @Override
                    public void onFinish() {
                        hideProgressDialog();
                    }

                }));
    }

    private void handleOtpError() {
        super.displayAlertDialog(getString(R.string.dialog_message_otp_error), false);
    }

    private void otpSuccessHandler(OTPResponseModel model) {
        if (model != null && model.getOTPData() != null && model.getOTPData().getSessionKey() != null) {
            mSupplier.setSessionToken(model.getOTPData().getSessionKey());
//            if (model.getOTPData().getDeviceType().equalsIgnoreCase("cab")) {
            mSupplier.setDeviceType(DeviceType.DEVICE_TYPE_TV);
            /*} else {
                mSupplier.setDeviceType(DeviceType.DEVICE_TYPE_TV);
            }*/
            startMainActivity();
        } else {
            handleOtpError();
        }
    }

    @Override
    public void otpUserResponse(String otpMessage, String deviceType) {

        if (otpMessage != null && deviceType != null) {
            processOTPRequest(otpMessage, deviceType);
        }

    }

    @Override
    public void otpErrorResponse(String errorMessage) {
        super.displayAlertDialog(errorMessage, false);
    }

    /**
     * Provides a simple way of getting a device's location and is well suited for
     * applications that do not require a fine-grained location and that do not need location
     * updates. Gets the best and most recent location currently available, which may be null
     * in rare cases when a location is not available.
     * <p>
     * Note: this method should be called after location permission has been granted.
     */
   /* @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener( SplashActivity.this, task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Location mLastLocation = task.getResult();
                            mLatitude = String.valueOf(mLastLocation.getLatitude());
                            mLongitude = String.valueOf(mLastLocation.getLongitude());
                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
