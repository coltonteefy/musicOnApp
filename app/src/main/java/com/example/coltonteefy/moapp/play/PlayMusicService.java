package com.example.coltonteefy.moapp.play;

import android.app.Service;
import android.content.Intent;
import android.inputmethodservice.AbstractInputMethodService;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.coltonteefy.moapp.R;

import java.io.IOException;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PlayMusicService extends Service {

    private final String TAG = "PlayMusicService";
    MediaPlayer mediaPlayer = new MediaPlayer();
    String songUrl, coverArtUrl, artist, songTitle;
    int duration, min, sec;
    String tmpUpdateTime;


    public PlayMusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        songUrl = intent.getStringExtra("songUrl");
        coverArtUrl = intent.getStringExtra("coverArtUrl");
        artist = intent.getStringExtra("artist");
        songTitle = intent.getStringExtra("songTitle");
        Log.i(TAG, "onStartCommand: INSIDE PLAY SERVICE "
                + "\n" + songUrl
                + "\n" + coverArtUrl
                + "\n" + artist
                + "\n" + songTitle);

        Intent updateIntent = new Intent();
        updateIntent.setAction("newSongInfo");
        updateIntent.putExtra("coverArtUrl", coverArtUrl);
        updateIntent.putExtra("artist", artist);
        updateIntent.putExtra("songTitle", songTitle);
        updateIntent.putExtra("songURL", songUrl);
        LocalBroadcastManager.getInstance(PlayMusicService.this).sendBroadcast(updateIntent);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
