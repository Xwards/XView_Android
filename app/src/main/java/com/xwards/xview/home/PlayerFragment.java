package com.xwards.xview.home;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.xwards.xview.R;
import com.xwards.xview.base.BaseFragment;
import com.xwards.xview.respmodel.AdVideoModel;
import com.xwards.xview.utils.AppLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PagerCallBack} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class PlayerFragment extends BaseFragment implements MediaPlayer.OnCompletionListener,
        Player.EventListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    private List<AdVideoModel> mAdvList = new ArrayList<>();
    private PagerCallBack mListener;
    private String TAG = "PlayerFragment";
    private AdVideoModel mAdvObject;
    private ImageView mIvPreview;
    private DownloadManager mDownloadManager;
    private PlayerView mVvLocalVideo;
    private FrameLayout mFlRoot;
    private SimpleExoPlayer player;
    private int CURRENT_VIDEO_POSITION = -1;
    private BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {
            long referenceId = intent.getLongExtra(android.app.DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            String path = null;
            try {
                PlayerFragment.super.hideProgressDialog();
                if (mAdvObject != null) {
                    playVideo(mAdvObject.getAdvUrl());
                }
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
        }
    };

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAdvList = getArguments().getParcelableArrayList("video_list");
            this.mDownloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mIvPreview = view.findViewById(R.id.iv_video_preview);
        this.mVvLocalVideo = view.findViewById(R.id.vv_local_video);
        this.mFlRoot = view.findViewById(R.id.fl_root);
        this.mFlRoot.setOnClickListener(v -> {
            Log.i(TAG, "Item Clicked");
        });
        AppLog.writeToFile("View Created");

        loadNextVideo();

    }

    private void loadNextVideo() {
        if (mAdvList == null || mAdvList.isEmpty()) {
            return;
        }
        if (CURRENT_VIDEO_POSITION == -1) {
            //Assume that this is the first Video
            CURRENT_VIDEO_POSITION = 0;
            mAdvObject = mAdvList.get(CURRENT_VIDEO_POSITION);
        } else {
            CURRENT_VIDEO_POSITION = CURRENT_VIDEO_POSITION + 1;
            if (CURRENT_VIDEO_POSITION >= mAdvList.size()) {
                CURRENT_VIDEO_POSITION = 0;
            }
            mAdvObject = mAdvList.get(CURRENT_VIDEO_POSITION);
        }
        playVideo(mAdvObject.getAdvUrl());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player != null &&
                (player.getPlaybackState() == ExoPlayer.STATE_IDLE ||
                        player.getPlaybackState() == ExoPlayer.STATE_ENDED)) {
            restartPlayer();
        }
        registerBroadCast();

    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            if (player != null) {
                pausePlayer();
                AppLog.writeToFile("Stop Player");
            }
            unRegisterBroadCast();

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private void pausePlayer() {
        try {
           /* player.setPlayWhenReady(false);
            player.getPlaybackState();*/
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private void restartPlayer() {
        try {
            /*player.setPlayWhenReady(true);
            player.getPlaybackState();*/
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PagerCallBack) {
            mListener = (PagerCallBack) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void playVideo(String url) {

        if (url == null || url.length() == 0) {
            return;
        }
        try {
            AppLog.writeToFile("Video Play Started");
            String path = isAlreadyExist(Uri.parse(url));
            Uri mp4VideoUri;
            if (path == null) {
                if (super.isNetworkConnectionAvailable(getActivity())) {
                    super.showProgressDialog(getString(R.string.alert_dialog_download_started));
                    downloadVideo(url, mAdvObject.getAdvId());
                    return;
                }
                return;
            } else {
                mp4VideoUri = Uri.parse(path);
            }
            if (mp4VideoUri != null) {
                TrackSelection.Factory videoTrackSelectionFactory =
                        new AdaptiveTrackSelection.Factory(null);
                TrackSelector trackSelector =
                        new DefaultTrackSelector(videoTrackSelectionFactory);
                player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
                player.addListener(this);
                mVvLocalVideo.setPlayer(player);
                mVvLocalVideo.hideController();
                mVvLocalVideo.setOnClickListener(this);
                // Measures bandwidth during playback. Can be null if not required.
                DefaultBandwidthMeter bandwidthMeterForPlay = new DefaultBandwidthMeter();
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                        Util.getUserAgent(getActivity(), "yourApplicationName"), bandwidthMeterForPlay);
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource videoSource = new ExtractorMediaSource(mp4VideoUri,
                        dataSourceFactory, extractorsFactory, null, null);
                player.prepare(videoSource);
                player.setPlayWhenReady(true);

                if(mListener != null){
                    mListener.updatePlayCount(mAdvObject);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    private void downloadVideo(String path, int id) {
        downloadFile(Uri.parse(path), "XView", "" + id, "advVideo");
    }

    private String isAlreadyExist(Uri uri) {
        try {
            String fileName = uri.toString().substring(uri.toString().lastIndexOf("/"));
            String outputFile = "sdcard//" + Environment.DIRECTORY_DOWNLOADS + "//XView//Videos/" + fileName;
            File file = new File(outputFile).getAbsoluteFile();
            if (file != null && file.exists() && file.canRead()) {
                return file.getAbsolutePath();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
            FirebaseCrash.logcat(Log.ERROR, TAG, "NPE caught");
            FirebaseCrash.report(e);
        }
        return null;
    }

    @Override
    protected boolean isNetworkConnectionAvailable(Context context) {
        return super.isNetworkConnectionAvailable(context);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (getActivity() == null) {
            return;
        }
        switch (playbackState) {
            case Player.STATE_ENDED: {
//                Toast.makeText(getActivity(), "Video ended", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Video ended" + mAdvObject.getAdvLocalPath());
                handlePlayerEnd();
            }
            break;
            case Player.STATE_BUFFERING: {
                Log.i(TAG, "Video Buffering" + mAdvObject.getAdvLocalPath());
//                Toast.makeText(getActivity(), "Video Buffering", Toast.LENGTH_SHORT).show();
            }
            break;
            case Player.STATE_READY: {
                Log.i(TAG, "Video Ready" + mAdvObject.getAdvLocalPath());
//                Toast.makeText(getActivity(), "Video Ready ", Toast.LENGTH_SHORT).show();
            }
            break;
            default: {
                Log.i(TAG, "Unknown Video Error State " + playbackState + "  Path " + mAdvObject.getAdvLocalPath());
//                Toast.makeText(getActivity(), "Unknown Video Error State " + playbackState, Toast.LENGTH_SHORT).show();
                handlePlayerEnd();
            }
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
//        Toast.makeText(getActivity(),"Player Exception "+error.getMessage(), Toast.LENGTH_LONG).show();
        if (getActivity() != null) {
            handlePlayerEnd();
        }
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    private void handlePlayerEnd() {
        if (player != null) {
            try {
//                player.stop();
                player.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        loadNextVideo();
        if (mListener != null) {
            mListener.videoCompletedCallBack(mAdvObject);
        }
    }

    public void downloadFile(Uri uri, String path, String title, String description) {
        String fileName = uri.toString().substring(uri.toString().lastIndexOf("/"));
        String mimeType = uri.toString().substring(uri.toString().lastIndexOf("."));
        android.app.DownloadManager.Request request = new android.app.DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(android.app.DownloadManager.Request.NETWORK_WIFI | android.app.DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(true);
        request.setTitle("Downloading " + getString(R.string.app_name));
        request.setDescription("Downloading " + "Videos");
        request.setVisibleInDownloadsUi(true);
        request.setMimeType(mimeType);

        File directory = new File(Environment.DIRECTORY_DOWNLOADS + "/XView/Videos");
        if (!directory.exists()) {
            directory.mkdirs();
        }
//        request.setDestinationInExternalPublicDir("/XView" , fileName);*/
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/XView/Videos" + fileName);
        long refid = mDownloadManager.enqueue(request);
    }

    public void registerBroadCast() {
        getActivity().registerReceiver(onComplete,
                new IntentFilter(android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void unRegisterBroadCast() {
        getActivity().unregisterReceiver(onComplete);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_root:
            case R.id.vv_local_video: {
                if (mVvLocalVideo != null && mVvLocalVideo.getVisibility() == View.VISIBLE && mListener != null) {
                    if (mAdvObject.getAdvSiteURL() == null || mAdvObject.getAdvSiteURL().isEmpty()) {
                        mAdvObject.setAdvSiteURL("https://www.google.com");
                    }
                    mListener.videoItemClickCallBack(mAdvObject);
                }
            }
            break;
        }
    }

    /**
     * The Update came from Main
     */

    public void updateAdList(ArrayList<AdVideoModel> modelList) {
        try {
            if (getActivity() == null) {
                return;
            }
            if (mAdvList != null && !mAdvList.isEmpty()) {
                synchronized (mAdvList) {
                    mAdvList = modelList;
                }
            } else {
                mAdvList = modelList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
