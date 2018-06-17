package com.xwards.xview.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.xwards.xview.R;
import com.xwards.xview.adapters.ClickListener;
import com.xwards.xview.adapters.EventAdapter;
import com.xwards.xview.base.BaseActivity;
import com.xwards.xview.base.BaseResponse;
import com.xwards.xview.database.AdvertDAO;
import com.xwards.xview.database.EventDAO;
import com.xwards.xview.dialogs.ContactUsCallBack;
import com.xwards.xview.dialogs.ContactUsDialog;
import com.xwards.xview.dialogs.DialogInterfaces;
import com.xwards.xview.dialogs.EventDialog;
import com.xwards.xview.dialogs.EventDialogCallBack;
import com.xwards.xview.enums.BrightnessEnum;
import com.xwards.xview.enums.DeviceType;
import com.xwards.xview.reqmodel.AnalyticsReqHeaderModel;
import com.xwards.xview.reqmodel.AnalyticsReqModel;
import com.xwards.xview.reqmodel.CommentsReqModel;
import com.xwards.xview.respmodel.AdVideoModel;
import com.xwards.xview.respmodel.AdvData;
import com.xwards.xview.respmodel.AnalyticsRespModel;
import com.xwards.xview.respmodel.EventModel;
import com.xwards.xview.respmodel.EventRespModel;
import com.xwards.xview.rest.APIRepository;
import com.xwards.xview.rest.NetworkCallBack;
import com.xwards.xview.rest.NetworkClient;
import com.xwards.xview.sp.SharedPrefDataSupplier;
import com.xwards.xview.utils.AdminReceiver;
import com.xwards.xview.utils.ScreenOffAdminReceiver;
import com.xwards.xview.web.WebActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements DialogInterfaces,
        PagerCallBack, View.OnClickListener, ClickListener, ContactUsCallBack, EventDialogCallBack {

    private static final String GET = "GET";
    private static final String INSERT = "INSERT";
    private static final String UPDATE = "UPDATE";
    private static final String DELETE = "DELETE";
    private static final String DONT_STAY_ON = "0";
    public static boolean IS_MAIN_RUNNING = false;
    private static String TAG = "MainActivity";
    protected PowerManager.WakeLock mWakeLock;
    private SharedPrefDataSupplier mSupplier;
    private ArrayList<AdVideoModel> mAdvList;
    private TextView mTvNoRecordFount;
    private ImageView mIvBannerAd;
    private FrameLayout mFlEventContainer;
    private ImageView mIvExpnadCollabse;
    private ImageView mIvExitkiosk;
    private Button mBtnContactUs;
    private View mDecorView;
    private RelativeLayout mRvPlayerContainer;
    private PlayerFragment mPlayerFragment;
    private Toolbar mToolBar;
    private ImageView mIvReset;
    private int PAGER_CURRENT_ITEM = 0;
    private MyTimer mTimer;
    private long COUNT_DOWN_INTERVAL = 1000;
    private long MILLIS_IN_FUTURE = 1000 * 60 * 2;
    private String mPreviousDateTime;
    private AdvertDAO mDAO;
    private FirebaseAnalytics mFirebaseAnalytics;
    private List<EventModel> mEventList = new ArrayList<>();
    private AdVideoModel mCurrentPlayingVideo;
    private DevicePolicyManager mDevicePolicyMngr;
    private List<AnalyticsReqModel> mAnalyticsList = new ArrayList<>();
    //Cosu
    private RecyclerView mRvEventList;
    private EventAdapter mEventAdapter;
    private View.OnClickListener mClickListener = view -> {
        switch (view.getId()) {
            case R.id.ivRightMenu: {
                MainActivity.super.displayAlertDialog(getString(R.string.alert_dialog_logout), true);
            }
            break;
        }
    };
    private DownloadManager mDownloadManager;
    private BroadcastReceiver mBatteryUpdateBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
//            Toast.makeText(MainActivity.this, "Screen Power Removed Receiver Called", Toast.LENGTH_SHORT).show();
            if (bundle != null) {
                if (bundle.containsKey(DEVICE_DISPLAY_OFF)) {
                    setScreenOff();
                }
            }
        }
    };

    private static long convertStringToLong(String time) {
        try {
            int quoteInd = time.indexOf(":");
            int pointInd = time.indexOf(".");

            int min = Integer.valueOf(time.substring(0, quoteInd));
            int sec = Integer.valueOf(time.substring(++quoteInd, pointInd));
            int mil = Integer.valueOf(time.substring(++pointInd, time.length()));

            return (((min * 60) + sec) * 1000) + mil;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSupplier = new SharedPrefDataSupplier(this);
        setContentView(R.layout.activity_main_cab);
        Log.i(TAG, "Session Key  " + mSupplier.getSessionToken());
        this.mDownloadManager = (android.app.DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        this.IS_MAIN_RUNNING = true;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Runtime.getRuntime().exec("dmp set-device_owner ");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Start Kiosk Mode
            ComponentName deviceAdmin = new ComponentName(this, AdminReceiver.class);
            mDevicePolicyMngr = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

            if (!mDevicePolicyMngr.isAdminActive(deviceAdmin)) {
                Toast.makeText(this, getString(R.string.not_device_admin), Toast.LENGTH_SHORT).show();
            }

            if (mDevicePolicyMngr.isDeviceOwnerApp(getPackageName())) {
                mDevicePolicyMngr.setLockTaskPackages(deviceAdmin, new String[]{getPackageName()});
            } else {
                Toast.makeText(this, getString(R.string.not_device_owner), Toast.LENGTH_SHORT).show();
            }
            mDecorView = getWindow().getDecorView();

            enableKioskMode();
        }
        muteVolume();
        mRvPlayerContainer = findViewById(R.id.rv_player_container);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mDAO = new AdvertDAO(this);
        Bundle bundle = getIntent().getExtras();
        int orientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;

        if (bundle != null) {
            orientation = bundle.getInt(DEVICE_ORIENTATION_KEY, ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }

        setRequestedOrientation(orientation);
        mAdvList = new ArrayList<>();
        mTvNoRecordFount = findViewById(R.id.tv_no_record_fount);
        mCompositeDisposable = new CompositeDisposable();
        mApiRepository = NetworkClient.getRetrofit().create(APIRepository.class);
        this.mToolBar = findViewById(R.id.toolbar);
        this.mIvReset = findViewById(R.id.ivRightMenu);
        this.mIvReset.setOnClickListener(mClickListener);
        this.mIvExitkiosk = findViewById(R.id.iv_exit_kiosk);
        this.mBtnContactUs = findViewById(R.id.btn_contact_us);
        this.mFlEventContainer = findViewById(R.id.fl_event_container);
        this.mIvBannerAd = findViewById(R.id.iv_banner_ad);
        this.mIvExpnadCollabse = findViewById(R.id.iv_expand_collapse);
        this.mIvExitkiosk.setOnClickListener(this);
        increaseTapAreaOfButton(mIvExitkiosk);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (mSupplier.getDeviceType() == DeviceType.DEVICE_TYPE_CAB) {
            initCabUi();
        } else {
            initTvUi();
        }
        viewHideToolBar(false);
        keepTheScreenOn();


        getData();
        startTimer();
        mPreviousDateTime = mSupplier.getLastUpdateTime();
        checkBrightnessStatus();
        /**
         * CALLING SERVICE
         */
    }

    private void initTvUi() {
        mIvBannerAd.setVisibility(View.GONE);
        mFlEventContainer.setVisibility(View.GONE);
        mIvExpnadCollabse.setVisibility(View.GONE);
        mBtnContactUs.setVisibility(View.GONE);
    }

    public final void setScreenOff() {
        try {
            turnScreenOff(getApplicationContext());
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private void initCabUi() {
        mIvExpnadCollabse.setVisibility(View.VISIBLE);
        mFlEventContainer.setVisibility(View.VISIBLE);
        mBtnContactUs.setVisibility(View.VISIBLE);
        increaseTapAreaOfButton(mIvExpnadCollabse);
        expandCollapseItem(true);
        mIvExpnadCollabse.setOnClickListener(this);
        mBtnContactUs.setOnClickListener(this);
        mRvEventList = findViewById(R.id.rv_event_list);
        mIvBannerAd.setOnClickListener(this);

        if (super.isNetworkConnectionAvailable(this)) {
            getEventListFromServer(mSupplier.getSessionToken(), "");
        } else {
            getEventListFromDb();
        }
    }

    private void getEventListFromDb() {
        EventDAO dao = new EventDAO(this);
        mEventList.addAll(dao.getAllEventData());

        if (mEventList != null && !mEventList.isEmpty()) {
            setEventAdapter(mEventList);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void getData() {
        try {
            if (super.isNetworkConnectionAvailable(this)) {
                if (checkPermission()) {
                    advRequestHandler();
                }
            } else {
                mAdvList.addAll(mDAO.getAllAdvList());
                setPlayerFragment();
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            FirebaseCrash.logcat(Log.ERROR, TAG, "NPE caught");
            FirebaseCrash.report(e);
        }
    }

    private void keepTheScreenOn() {
        /**
         * Keep the Screen On
         */
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();
    }

    @Override
    public void updateDialogResult(String message) {
        if (message.equalsIgnoreCase(getString(R.string.alert_dialog_logout))) {
            logout();
        } else if (message.equalsIgnoreCase(getString(R.string.alert_dialog_invalid_kiosk_id))) {
            showExitKioskDialog();
        }
    }

    private void logout() {
        mSupplier.removeData();
        this.finish();
    }

    private void advRequestHandler() {
        try {
            if (super.isNetworkConnectionAvailable(this)) {
                getAdvList("", "video");
            } else {
                super.displayNetworkErrorDialog();
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
            FirebaseCrash.logcat(Log.ERROR, TAG, "NPE caught");
            FirebaseCrash.report(e);
        }
    }

    @Override
    public void videoItemClickCallBack(AdVideoModel adVideoModel) {
        if (adVideoModel == null) {
            return;
        }
        if (mAnalyticsList == null || mAnalyticsList.isEmpty()) {
            return;
        }

        if (adVideoModel.getAdvSiteURL() == null) {
            adVideoModel.setAdvSiteURL("https://www.facebook.com/xwards/");
            startWebActivity(adVideoModel.getAdvSiteURL(), super.SITE_REQ_CODE_FOR_AD);
        }
        for (int i = 0; i < mAnalyticsList.size(); i++) {
            if (mAnalyticsList.get(i).getAdvId() == adVideoModel.getAdvId()) {
                mAnalyticsList.get(i).setAdvTouchCount(mAnalyticsList.get(i).getAdvTouchCount() + 1);
            }
        }
    }

    @Override
    public void updatePlayCount(AdVideoModel mAdvObject) {
        if (mAdvObject == null) {
            return;
        }
        mCurrentPlayingVideo = mAdvObject;

        handleBannerData(mCurrentPlayingVideo.getBannerUrl());

        if (mAnalyticsList == null || mAnalyticsList.isEmpty()) {
            return;
        }
        for (int i = 0; i < mAnalyticsList.size(); i++) {
            if (mAnalyticsList.get(i).getAdvId() == mAdvObject.getAdvId()) {
                mAnalyticsList.get(i).setAdvPlayCount(mAnalyticsList.get(i).getAdvPlayCount() + 1);
            }
        }
    }

    private void startWebActivity(String siteURL, int reqCode) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(super.WEB_VIEW_URL_KEY, siteURL);
        startActivityForResult(intent, reqCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == super.SITE_REQ_CODE_FOR_AD && resultCode == RESULT_OK) {
            if (data != null) {
                long value = data.getLongExtra(SPEND_HOURS, 0);
                if (mCurrentPlayingVideo != null) {
                    for (int i = 0; i < mAnalyticsList.size(); i++) {
                        if (mAnalyticsList.get(i).getAdvId() == mCurrentPlayingVideo.getAdvId()) {
                            long time = convertStringToLong(mAnalyticsList.get(i).getAdvActiveTime()) + value;
                            mAnalyticsList.get(i).setAdvActiveTime(longToHours(time));
                        }
                    }
                }
            }
        } else if (requestCode == super.SITE_REQ_CODE_FOR_EVENT && resultCode == RESULT_OK) {

        }
    }

    @Override
    protected void onPause() {

        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBatteryUpdateBroadcast);
    }

    /**
     * Returns the unique identifier for the device
     *
     * @return unique identifier for the device
     */
    @SuppressLint("MissingPermission")
    public String getDeviceIMEI() {
        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            deviceUniqueIdentifier = tm.getDeviceId();
        }
        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceUniqueIdentifier;
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            advRequestHandler();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBatteryUpdateBroadcast,
                new IntentFilter(BROADCAST_BATTERY_DATA_UPDATE));

        hideSystemUI();
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= 23) {
            // Set the IMMERSIVE flag.
            // Set the content to appear under the system bars so that the content
            // doesn't resize when the system bars hide and show.
            try {
                mDecorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, e.getMessage());
            }

        }
    }


    private void getAdvList(String previousPingDate, String requestType) {
        try {
            if (!super.isNetworkConnectionAvailable(this)) {
                super.displayNetworkErrorDialog();
                return;
            }
            super.showProgressDialog("Loading...");
            mCompositeDisposable.add(mApiRepository.getAdvertisement(mSupplier.getSessionToken(),
                    requestType, previousPingDate).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).
                    subscribeWith(new NetworkCallBack<AdvData>() {
                        @Override
                        public void onComplete() {
                            hideProgressDialog();
                        }

                        @Override
                        public void onSuccess(AdvData model) {
                            //                        view.getDataSuccess(model);
                            hideProgressDialog();
                            responseHandler(model);
                        }

                        @Override
                        public void onFailure(String message, String errorCode) {
                            hideProgressDialog();
                            //                        view.showError(message, errorCode);
                        }

                        @Override
                        public void onFinish() {
                            hideProgressDialog();
                        }
                    }));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
            FirebaseCrash.logcat(Log.ERROR, TAG, "NPE caught");
            FirebaseCrash.report(e);
        }
    }

    private void callHeartBeat(String requestType, String lastUpdateDate) {
        try {
            if (!super.isNetworkConnectionAvailable(this)) {
                // super.displayNetworkErrorDialog();
                return;
            }

            mCompositeDisposable.add(mApiRepository.getAdvertisement(mSupplier.getSessionToken(),
                    requestType, lastUpdateDate).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).
                    subscribeWith(new NetworkCallBack<AdvData>() {
                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onSuccess(AdvData model) {
                            mPreviousDateTime = getCurrentDate();
                            mSupplier.setLastUpdatedTime(mPreviousDateTime);
                            heartBeatResponseHandler(model);
                        }

                        @Override
                        public void onFailure(String message, String errorCode) {
                            //                        view.showError(message, errorCode);
                        }

                        @Override
                        public void onFinish() {

                        }
                    }));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
            FirebaseCrash.logcat(Log.ERROR, TAG, "NPE caught");
            FirebaseCrash.report(e);
        }
    }


    private void updateUserComment(String sessionToken, CommentsReqModel model) {
        try {
            if (!super.isNetworkConnectionAvailable(this)) {
                super.displayNetworkErrorDialog();
                return;
            }
            mCompositeDisposable.add(mApiRepository.updateUserComments(sessionToken, model).
                    observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).
                    subscribeWith(new NetworkCallBack<BaseResponse>() {
                        @Override
                        public void onComplete() {
                            MainActivity.super.hideProgressDialog();
                        }

                        @Override
                        public void onSuccess(BaseResponse model) {
                            MainActivity.super.hideProgressDialog();
                            if (model != null) {
                                displayAlertDialog(getString(R.string.alert_dialog_feedback_success), false);
                            }
                        }

                        @Override
                        public void onFailure(String message, String errorCode) {
                            //                        view.showError(message, errorCode);
                            MainActivity.super.hideProgressDialog();
                        }

                        @Override
                        public void onFinish() {
                            MainActivity.super.hideProgressDialog();
                        }
                    }));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
            FirebaseCrash.logcat(Log.ERROR, TAG, "NPE caught");
            FirebaseCrash.report(e);
        }
    }


    private void callAnalytics(String sessionToken, AnalyticsReqHeaderModel model) {
        try {

            if (!super.isNetworkConnectionAvailable(this)) {
//                super.displayNetworkErrorDialog();
                return;
            }

            mCompositeDisposable.add(mApiRepository.updateAnalytics(sessionToken, model).
                    observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).
                    subscribeWith(new NetworkCallBack<AnalyticsRespModel>() {
                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onSuccess(AnalyticsRespModel model) {
                            analyticsUpdateCallBack(model);
                        }

                        @Override
                        public void onFailure(String message, String errorCode) {
                            //                        view.showError(message, errorCode);
                        }

                        @Override
                        public void onFinish() {

                        }
                    }));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
            FirebaseCrash.logcat(Log.ERROR, TAG, "NPE caught");
            FirebaseCrash.report(e);
        }
    }

    private void increaseTapAreaOfButton(ImageView imageButton) {
        final View parent = (View) imageButton.getParent();  // button: the view you want to enlarge hit area
        parent.post(() -> {
            final Rect rect = new Rect();
            imageButton.getHitRect(rect);
            rect.top -= 100;    // increase top hit area
            rect.left -= 100;   // increase left hit area
            rect.bottom += 100; // increase bottom hit area
            rect.right += 100;  // increase right hit area
            parent.setTouchDelegate(new TouchDelegate(rect, imageButton));
        });
    }

    /**
     * To Fetch the Event List Information from the Server
     *
     * @param sessionToken - Current Session Token
     */
    private void getEventListFromServer(String sessionToken, String dateTime) {
        try {
            if (!super.isNetworkConnectionAvailable(this)) {
//                super.displayNetworkErrorDialog();
                return;
            }
            mCompositeDisposable.add(mApiRepository.getEventList(sessionToken, dateTime).
                    observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).
                    subscribeWith(new NetworkCallBack<EventRespModel>() {
                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onSuccess(EventRespModel model) {
                            updateEventRespData(model);

                        }

                        @Override
                        public void onFailure(String message, String errorCode) {

                        }

                        @Override
                        public void onFinish() {

                        }
                    }));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
            FirebaseCrash.logcat(Log.ERROR, TAG, "NPE caught");
            FirebaseCrash.report(e);
        }
    }

    /**
     * To Process the Response from Event
     *
     * @param model - Event Model
     */
    private void updateEventRespData(EventRespModel model) {
        if (model != null &&
                model.getEventData() != null &&
                model.getEventData().getEventList() != null &&
                !model.getEventData().getEventList().isEmpty()) {

            EventDAO eventDAO = new EventDAO(this);
            if (mEventList.isEmpty()) {
                mEventList.addAll(model.getEventData().getEventList());
                setEventAdapter(mEventList);
                for (EventModel object : mEventList) {
                    eventDAO.insertObject(object);
                }
            } else {
                /**
                 * For Adding
                 */
                List<EventModel> addItemList = new ArrayList<>();
                for (EventModel newObject : model.getEventData().getEventList()) {
                    boolean isPresent = false;
                    for (EventModel existingObj : mEventList) {
                        if (existingObj.getEventId() == newObject.getEventId()) {
                            isPresent = true;
                            break;
                        }
                    }
                    if (!isPresent) {// This is a new entry- add it to the list
                        addItemList.add(newObject);
                    }
                }
                /**
                 * For Removing
                 */
                List<EventModel> removeItemList = new ArrayList<>();
                for (EventModel existingObj : mEventList) {
                    boolean isPresent = false;
                    for (EventModel newObject : model.getEventData().getEventList()) {
                        if (existingObj.getEventId() == newObject.getEventId()) {
                            isPresent = true;
                            break;
                        }
                    }
                    if (!isPresent) {// This entry is not in the server side
                        removeItemList.add(existingObj);
                    }
                }

                /**
                 * Operation
                 */
                boolean hasChange = false;
                if (!removeItemList.isEmpty()) {
                    hasChange = true;
                    mEventList.removeAll(removeItemList);
                    //Remove from DB
                    for (EventModel removeItem : removeItemList) {
                        eventDAO.deleteFromTable(removeItem.getEventId());
                    }
                }
                if (!addItemList.isEmpty()) {
                    hasChange = true;
                    mEventList.addAll(addItemList);
                    //Add To DB
                    for (EventModel addItem : removeItemList) {
                        eventDAO.insertObject(addItem);
                    }
                }
                if (hasChange) {
                    //update to UI
                    setEventAdapter(mEventList);
                }
            }


        }
    }

    private void analyticsUpdateCallBack(AnalyticsRespModel model) {

    }

    private void heartBeatResponseHandler(AdvData model) {
        try {
            if (model != null && model.getAdvCollection() != null) {
                setKisokNumber(model.getAdvCollection().getKisokExitNumber());

                if (mSupplier.getDeviceType() == DeviceType.DEVICE_TYPE_TV &&
                        model.getAdvCollection().getDeviceType().equalsIgnoreCase("cab")) {
                    initCabUi();
                } else if (mSupplier.getDeviceType() == DeviceType.DEVICE_TYPE_CAB &&
                        model.getAdvCollection().getDeviceType().equalsIgnoreCase("tv-box")) {
                    initTvUi();
                }
                setDeviceType(model.getAdvCollection().getDeviceType());
            }
            if (model != null && model.getAdvCollection() != null &&
                    model.getAdvCollection().getAdVideoList() != null &&
                    !model.getAdvCollection().getAdVideoList().isEmpty()) {

                if (mAdvList == null || mAdvList.isEmpty()) {
                    mAdvList.addAll(model.getAdvCollection().getAdVideoList());
                    mAnalyticsList = getAnalyticsList();
                    setPlayerFragment();
                    mTvNoRecordFount.setVisibility(View.GONE);
                    for (AdVideoModel obj : mAdvList) {
                        mDAO.insertObject(obj);
                    }
                } else {
                    /**
                     * For Adding
                     */
                    List<AdVideoModel> addItemList = new ArrayList<>();
                    for (AdVideoModel newObject : model.getAdvCollection().getAdVideoList()) {
                        boolean isPresent = false;
                        for (AdVideoModel existingObj : mAdvList) {
                            if (existingObj.getAdvId() == newObject.getAdvId()) {
                                isPresent = true;
                                break;
                            }
                        }
                        if (!isPresent) {// This is a new entry- add it to the list
                            addItemList.add(newObject);
                        }
                    }
                    /**
                     * For Removing
                     */
                    List<AdVideoModel> removeItemList = new ArrayList<>();
                    for (AdVideoModel existingObj : mAdvList) {
                        boolean isPresent = false;
                        for (AdVideoModel newObject : model.getAdvCollection().getAdVideoList()) {
                            if (existingObj.getAdvId() == newObject.getAdvId()) {
                                isPresent = true;
                                break;
                            }
                        }
                        if (!isPresent) {// This entry is not in the server side
                            removeItemList.add(existingObj);
                        }
                    }

                    /**
                     * Operation
                     */
                    boolean hasChange = false;
                    if (!removeItemList.isEmpty()) {
                        hasChange = true;
                        mAdvList.removeAll(removeItemList);
                        //Remove from DB
                        for (AdVideoModel removeItem : removeItemList) {
                            mDAO.deleteFromTable(removeItem.getAdvId());
                        }
                    }
                    if (!addItemList.isEmpty()) {
                        hasChange = true;
                        mAdvList.addAll(addItemList);
                        //Add To DB
                        for (AdVideoModel addItem : removeItemList) {
                            mDAO.insertObject(addItem);
                        }
                    }
                    if (hasChange && (mPlayerFragment != null) && (mPlayerFragment.isVisible())) {
                        //update to UI
                        mPlayerFragment.updateAdList(mAdvList);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
            FirebaseCrash.logcat(Log.ERROR, TAG, "NPE caught");
            FirebaseCrash.report(e);
        }
    }

    private void responseHandler(AdvData data) {
        if (data == null) {
            return;
        }
        if (data.getAdvCollection() == null) {
            return;
        }

        setKisokNumber(data.getAdvCollection().getKisokExitNumber());
        setDeviceType(data.getAdvCollection().getDeviceType());

        if (data != null && data.getAdvCollection() != null && data.getAdvCollection().getAdVideoList() != null) {
            mAdvList.addAll(data.getAdvCollection().getAdVideoList());
            mAnalyticsList = getAnalyticsList();
            setPlayerFragment();
            mTvNoRecordFount.setVisibility(View.GONE);
            for (AdVideoModel obj : mAdvList) {
                mDAO.insertObject(obj);
            }
        } else {
            mTvNoRecordFount.setVisibility(View.VISIBLE);
        }
    }

    private void setPlayerFragment() {
        try {
            if (mAdvList == null || mAdvList.isEmpty()) {
                return;
            }
            if (mPlayerFragment == null) {
                Bundle data = new Bundle();
                data.putParcelableArrayList("video_list", mAdvList);
                mPlayerFragment = new PlayerFragment();
                mPlayerFragment.setArguments(data);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().add(R.id.rv_player_container, mPlayerFragment, "Video Player").commit();
            } else {
                mPlayerFragment.updateAdList(mAdvList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
            FirebaseCrash.logcat(Log.ERROR, TAG, "NPE caught");
            FirebaseCrash.report(e);
        }
    }

    private void setDeviceType(String type) {
        if (type == null || type.isEmpty()) {
            return;
        }

        if (type.equalsIgnoreCase("cab")) {
            mSupplier.setDeviceType(DeviceType.DEVICE_TYPE_CAB);
        } else {
            mSupplier.setDeviceType(DeviceType.DEVICE_TYPE_TV);
        }
    }

    private void setKisokNumber(String kisokNumber) {
        if (kisokNumber == null || kisokNumber.isEmpty()) {
            return;
        }
        mSupplier.setKisokNumber(kisokNumber);
    }

    private void viewHideToolBar(boolean isView) {
        if (isView) {
            mToolBar.setVisibility(View.VISIBLE);
        } else {
            mToolBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void videoCompletedCallBack(AdVideoModel adVideoModel) {
        try {
            PAGER_CURRENT_ITEM = PAGER_CURRENT_ITEM + 1;
            if (PAGER_CURRENT_ITEM >= mAdvList.size()) {
                PAGER_CURRENT_ITEM = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
            FirebaseCrash.logcat(Log.ERROR, TAG, "NPE caught");
            FirebaseCrash.report(e);
        }
    }

    private void handleBannerData(String bannerUrl) {

        try {
            if (bannerUrl == null || bannerUrl.isEmpty()) {
                return;
            }
            if (mSupplier.getDeviceType() == DeviceType.DEVICE_TYPE_TV) {
                return;
            }
            String fileName = bannerUrl.toString().substring(bannerUrl.toString().lastIndexOf("/"));
            String outputFile = "sdcard//" + Environment.DIRECTORY_DOWNLOADS + "//XView//Banners/" + fileName;
            File file = new File(outputFile);
            if (super.isNetworkConnectionAvailable(this)) {
                String path = null;
                if (isFileExist(fileName, file)) {
                    path = file.getAbsolutePath();
                } else {
                    path = bannerUrl;
                    downloadBannerAd(Uri.parse(bannerUrl));
                }
                Glide.with(this).
                        load(path).
                        into(mIvBannerAd);
            } else {
                // Check Local Availability
                if (file.exists()) {
                    Glide.with(this).
                            load(file.getAbsolutePath()).
                            into(mIvBannerAd);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    private boolean isFileExist(String fileName, File file) {

        if (fileName == null) {
            return false;
        }

        if (file.exists()) {
            return true;
        }
        return false;
    }


    private void downloadBannerAd(Uri bannerUrl) {
        String fileName = bannerUrl.toString().substring(bannerUrl.toString().lastIndexOf("/"));
        String mimeType = bannerUrl.toString().substring(bannerUrl.toString().lastIndexOf("."));
        android.app.DownloadManager.Request request = new android.app.DownloadManager.Request(bannerUrl);
        request.setAllowedNetworkTypes(android.app.DownloadManager.Request.NETWORK_WIFI | android.app.DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("Downloading " + getString(R.string.app_name));
        request.setDescription("Downloading " + "Banner");
        request.setVisibleInDownloadsUi(true);
        request.setMimeType(mimeType);

        File directory = new File(Environment.DIRECTORY_DOWNLOADS + "/XView/Banners");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/XView/Banners" + fileName);
        long refid = mDownloadManager.enqueue(request);
    }

    private void startTimer() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = new MyTimer(MILLIS_IN_FUTURE, COUNT_DOWN_INTERVAL);
        mTimer.start();
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private List<AnalyticsReqModel> getAnalyticsList() {
        List<AnalyticsReqModel> modelList = new ArrayList<>();
        for (AdVideoModel model : mAdvList) {
            AnalyticsReqModel analyticsReqModel = new AnalyticsReqModel();
            analyticsReqModel.setAdvId(model.getAdvId());
            modelList.add(analyticsReqModel);
        }
        return modelList;
    }

    private String longToHours(long ms) {

        try {
            int SECOND = 1000;
            int MINUTE = 60 * SECOND;
            int HOUR = 60 * MINUTE;
            int DAY = 24 * HOUR;
            StringBuffer text = new StringBuffer("");
            if (ms > DAY) {
                text.append(ms / DAY).append(" days ");
                ms %= DAY;
            }
            if (ms > HOUR) {
                text.append(ms / HOUR).append(" hours ");
                ms %= HOUR;
            }
            if (ms > MINUTE) {
                text.append(ms / MINUTE).append(" minutes ");
                ms %= MINUTE;
            }
            if (ms > SECOND) {
                text.append(ms / SECOND).append(" seconds ");
                ms %= SECOND;
            }
            text.append(ms + " ms");
            System.out.println(text.toString());
            return text.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
        return "0";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mWakeLock.release();
        this.IS_MAIN_RUNNING = false;
        stopTimer();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            Bundle data = intent.getExtras();
            if (data == null) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    protected String getCurrentDate() {
        Date date = new Date();
        String strDateFormat = "yyy/MM/dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat, Locale.getDefault());

        return sdf.format(date);
    }

    /**
     * Firebase Analytics
     */
    private void sendAnalytics(String videoId, String videoName) {
       /* Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, videoId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, videoName);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "video");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_exit_kiosk: {
                showExitKioskDialog();
            }
            break;
            case R.id.iv_expand_collapse: {
                expandCollapseItem(false);
            }
            break;
            case R.id.btn_contact_us: {
                showContactUsDialog();
            }
            break;
            case R.id.iv_banner_ad: {
                videoItemClickCallBack(mCurrentPlayingVideo);
            }
            break;
        }
    }

    private void showContactUsDialog() {
        FragmentManager manager = getSupportFragmentManager();
        ContactUsDialog dialog = new ContactUsDialog();
        dialog.show(manager, "OTP");
    }

    @Override
    public void onClick(View view, int position) {

        if (mEventList == null) {
            return;
        }

        if (position < mEventList.size()) {
            callEventDialog(mEventList.get(position));
        }
    }

    private void callEventDialog(EventModel eventModel) {
        FragmentManager manager = getSupportFragmentManager();
        Bundle arg = new Bundle();
        arg.putParcelable(EventDialogCallBack.EVENT_DIALOG_OBJ_KEY, eventModel);
        EventDialog dialog = new EventDialog();
        dialog.setArguments(arg);
        dialog.show(manager, "OTP");
    }

    /**
     * Enable Kiosk Mode-
     * This will Help the Application to run the device as a
     * EMM(Enterprise Mobility Management)
     * Application, It means the device is a single owner single purpose.
     */
    private void enableKioskMode() {
        if (Build.VERSION.SDK_INT >= 23) {
            startLockTask();
        }
    }

    /**
     * To Exit from Kiosk Mode
     */
    private void disableKioskMode() {
        if (Build.VERSION.SDK_INT >= 23) {
            stopLockTask();
        }
    }

    /**
     * Expand Collapse Frame Items
     */
    private void expandCollapseItem(boolean forceVisible) {

        if (forceVisible) {
            mIvBannerAd.setVisibility(View.VISIBLE);
            mFlEventContainer.setVisibility(View.VISIBLE);
            mIvExpnadCollabse.setBackgroundResource(R.mipmap.expand);

            return;
        }

        if (mIvBannerAd.getVisibility() == View.VISIBLE &&
                mFlEventContainer.getVisibility() == View.VISIBLE) {
            mIvBannerAd.setVisibility(View.GONE);
            mFlEventContainer.setVisibility(View.GONE);
            mIvExpnadCollabse.setBackgroundResource(R.mipmap.collapse);
        } else {
            mIvBannerAd.setVisibility(View.VISIBLE);
            mFlEventContainer.setVisibility(View.VISIBLE);
            mIvExpnadCollabse.setBackgroundResource(R.mipmap.expand);
//            getEventListFromServer(mSupplier.getSessionToken(), "");
        }
    }

    private void showExitKioskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.alert_dialog_otp_message));
        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().equalsIgnoreCase(mSupplier.getKisokNumber())) {
                    disableKioskMode();
                } else {
                    displayAlertDialog(getString(R.string.alert_dialog_invalid_kiosk_id), false);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void setEventAdapter(List<EventModel> eventList) {
        if (mEventAdapter == null) {
            mEventAdapter = new EventAdapter(this, eventList);
            mEventAdapter.setClickListener(this);
            mRvEventList.setAdapter(mEventAdapter);
            mRvEventList.setLayoutManager(new LinearLayoutManager(this));
        } else {
            notifyEventAdapter();
        }
    }

    private void notifyEventAdapter() {
        if (mEventAdapter != null) {
            mEventAdapter.notifyDataSetChanged();
        }
    }

    private void clearAnalyticsData() {
        if (mAnalyticsList != null && !mAnalyticsList.isEmpty()) {
            for (AnalyticsReqModel obj : mAnalyticsList) {
                obj.setAdvPlayCount(0);
                obj.setAdvTouchCount(0);
                obj.setAdvActiveTime("0");
            }
        }
    }

    @Override
    public void updateUserComments(CommentsReqModel model) {
        if (super.isNetworkConnectionAvailable(this)) {
            super.showProgressDialog(getString(R.string.alert_dialog_message_updating));
            updateUserComment(mSupplier.getSessionToken(), model);
        } else {
            super.displayNetworkErrorDialog();
        }
    }

    @Override
    public void updateUserCmntsErrorStatus(String status) {
        super.displayAlertDialog(status, false);
    }

    @Override
    public void updateEventObject(EventModel object) {
        if (object != null) {
            startWebActivity(object.getEventUrl(), super.SITE_REQ_CODE_FOR_EVENT);
        }
    }

    private void muteVolume() {
        try {
            AudioManager amanager = (AudioManager) getSystemService(AUDIO_SERVICE);
            amanager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
            amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
            amanager.setStreamMute(AudioManager.STREAM_ALARM, true);
            amanager.setStreamMute(AudioManager.STREAM_RING, true);
            amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    private class MyTimer extends CountDownTimer {

        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            Log.d(TAG, "TICK " + l);
        }

        @Override
        public void onFinish() {

            setSbHandler();

            if (mPreviousDateTime == null) {
                mPreviousDateTime = getCurrentDate();
                mSupplier.setLastUpdatedTime(mPreviousDateTime);
            }
            if (isNetworkConnectionAvailable(MainActivity.this)) {
                callHeartBeat("video", "");
            } else if (mAdvList == null || mAdvList.isEmpty()) {
                //Check Locally
                mAdvList.addAll(mDAO.getAllAdvList());
            }
            AnalyticsReqHeaderModel reqModel = new AnalyticsReqHeaderModel();
            reqModel.setAnalyticsList(mAnalyticsList);

            callAnalytics(mSupplier.getSessionToken(), reqModel);
            clearAnalyticsData();
            startTimer();
        }
    }

    private void setSbHandler() {
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY); //Current hour
        if (currentHour >= 4 && currentHour <= 7) {
            scheduleBrightness(BrightnessEnum.ENUM_MORNING_TIME);
        } else if (currentHour >= 7 && currentHour <= 17) {
            scheduleBrightness(BrightnessEnum.ENUM_DAY_TIME);
        } else if (currentHour >= 17 && currentHour <= 19) {
            scheduleBrightness(BrightnessEnum.ENUM_EVENING_TIME);
        } else if (currentHour >= 19 || (currentHour >= 0  && currentHour <4)) {
            scheduleBrightness(BrightnessEnum.ENUM_NIGHT_TIME);
        }
    }

    /**
     * Schedule the brightness level of the device
     *
     * @param brightnessEnum - Enum class will manage the level and it has the documentation
     */
    private void scheduleBrightness(BrightnessEnum brightnessEnum) {

        switch (brightnessEnum) {
            case ENUM_MORNING_TIME: {
                ScreenBrightness(128);
            }
            break;
            case ENUM_DAY_TIME: {
                ScreenBrightness(255);
            }
            break;
            case ENUM_EVENING_TIME: {
                ScreenBrightness(128);
            }
            break;
            case ENUM_NIGHT_TIME: {
                ScreenBrightness(0);
            }
            break;
        }
    }

    private void checkBrightnessStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                // Do stuff here
               setSbHandler();
            } else {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else {
           setSbHandler();
        }
    }


    private boolean ScreenBrightness(int level) {

        try {
            android.provider.Settings.System.putInt(
                    this.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS, level);


            android.provider.Settings.System.putInt(this.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                    android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

            android.provider.Settings.System.putInt(
                    this.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS,
                    level);

            return true;
        }

        catch (Exception e) {
            Log.e("Screen Brightness", "error changing screen brightness");
            return false;
        }
    }

    static void turnScreenOff(final Context context) {
        DevicePolicyManager policyManager = (DevicePolicyManager) context
                .getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName adminReceiver = new ComponentName(context,
                ScreenOffAdminReceiver.class);
        boolean admin = policyManager.isAdminActive(adminReceiver);
        if (admin) {
            policyManager.lockNow();
        } else {
            Toast.makeText(context, R.string.device_admin_not_enabled,
                    Toast.LENGTH_LONG).show();
        }

    }

    private void activeManage() {
        Log.e(TAG, "activeManage");
        ComponentName componentName = new ComponentName(this, ScreenOffAdminReceiver.class);
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "developers：liushuaikobe");
        startActivityForResult(intent, 1);
    }
}

