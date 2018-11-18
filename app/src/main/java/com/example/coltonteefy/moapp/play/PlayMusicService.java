package com.example.coltonteefy.moapp.play;

import android.app.Service;
import android.content.Intent;
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

public class PlayMusicService extends Service {

    private final String TAG = "PlayMusicService";
    MediaPlayer mediaPlayer = new MediaPlayer();
    String songUrl, coverArtUrl, artist, songTitle;
    String tmpTimeLabel, tmpUpdateTime;
    Boolean isPlaying = false;
    int min, sec, duration;
    double startTime;
    private Handler myHandler = new Handler();

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

        try {
            mediaPlayer.setDataSource(songUrl);
            mediaPlayer.prepare();
            mediaPlayer.seekTo(0);
            mediaPlayer.setVolume(0.5f, 0.5f);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setLooping(true);

        mediaPlayer.start();
//        myHandler.postDelayed(UpdateSongTime, 100);

        setDuration(mediaPlayer);

        Intent updateIntent = new Intent();
        updateIntent.setAction("updateTime");
        updateIntent.putExtra("totalTime", getDurationTotal());
        updateIntent.putExtra("coverArtUrl", coverArtUrl);
        updateIntent.putExtra("artist", artist);
        updateIntent.putExtra("songTitle", songTitle);
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


    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public void totalTime(int time) {
        min = time / 1000 / 60;
        sec = time / 1000 % 60;
        tmpTimeLabel = min + ":";

        if (sec < 10) {
            tmpTimeLabel += "0";
        }
        tmpTimeLabel += sec;
//        Log.i(TAG, "TOTAL TIME: " + tmpTimeLabel);
//        totalTimeLabel.setText(String.valueOf(tmpTimeLabel));
    }

    public void updateTimeElapsed(int time) {
        min = time / 1000 / 60;
        sec = time / 1000 % 60;
        tmpUpdateTime = min + getString(R.string.colon);

        if (sec < 10) {
            tmpUpdateTime += getString(R.string.zero);
        }
        tmpUpdateTime += sec;
//        Log.i(TAG, "UPDATE TIME ELAPSED: " + tmpUpdateTime);
//        elapsedTimeLabel.setText(tmpUpdateTime);
    }

    public void setDuration(MediaPlayer mediaPlayer) {
        this.duration = mediaPlayer.getDuration();
    }

    public String getDurationTotal() {
        return String.valueOf(duration);
    }

    public int getCurrentPosition(MediaPlayer mediaPlayer) {
        return mediaPlayer.getCurrentPosition();
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            Log.i(TAG, "UPDATE SONG TIME: ");

//            startTime = mediaPlayer.getCurrentPosition();
//            updateTimeElapsed(mediaPlayer.getCurrentPosition());
//            songPositionBar.setProgress(mediaPlayer.getCurrentPosition());
            myHandler.postDelayed(this, 100);


            Intent intent = new Intent();
            intent.setAction("updateTime");
            intent.putExtra("elapsedTime", tmpUpdateTime);
            LocalBroadcastManager.getInstance(PlayMusicService.this).sendBroadcast(intent);
        }
    };
}
