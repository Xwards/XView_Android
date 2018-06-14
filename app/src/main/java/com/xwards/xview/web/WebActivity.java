package com.xwards.xview.web;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xwards.xview.R;
import com.xwards.xview.base.BaseActivity;

public class WebActivity extends BaseActivity {

    private WebView mWebView;
    private String mWebUrl;

    private long COUNTDOWN_INTERVAL = 1000;
    private long MILLIS_IN_FUTURE = 30000;


    private long mSpendTimeInMillis = 0;
    private MyTimer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Bundle data = getIntent().getExtras();

        if (data != null && data.containsKey(WEB_VIEW_URL_KEY)) {

            mWebUrl = data.getString(WEB_VIEW_URL_KEY);

            mWebView = findViewById(R.id.web_view);
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new WebViewClient() {
                @SuppressWarnings("deprecation")
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                }

                @TargetApi(android.os.Build.VERSION_CODES.M)
                @Override
                public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                    // Redirect to deprecated method, so you can use it in all SDK versions
                    onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
                }
            });
            mWebView.loadUrl(mWebUrl);

            startTimer();
        }
    }

    private void startTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        mTimer = new MyTimer(MILLIS_IN_FUTURE, COUNTDOWN_INTERVAL);
        mTimer.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startTimer();
        return super.onTouchEvent(event);
    }

    private void callParentActivity() {

        Intent intent = new Intent();
        intent.putExtra(SPEND_HOURS, mSpendTimeInMillis);
        setResult(Activity.RESULT_OK, intent);
        finish();

    }

    private class MyTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mSpendTimeInMillis = mSpendTimeInMillis + 1000;
        }

        @Override
        public void onFinish() {
            callParentActivity();
        }
    }
}
