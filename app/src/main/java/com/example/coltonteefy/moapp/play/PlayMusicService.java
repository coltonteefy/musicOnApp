package com.example.coltonteefy.moapp.play;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.coltonteefy.moapp.R;

import java.io.IOException;

public class PlayMusicService extends Service {

    MediaPlayer mediaPlayer;
    SeekBar songPositionBar;
    Boolean isPlaying = false;
    private Handler myHandler = new Handler();
    double startTime;
    int min, sec;
    String tmpTimeLabel, tmpUpdateTime;
    TextView elapsedTimeLabel, totalTimeLabel;

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

        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource("https://music-on-app.s3.us-west-1.amazonaws.com/uploads/musicUploads/1541199248759waitforyoulove.m4a");
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

//
//        mediaPlayer.seekTo(0);
//        mediaPlayer.setVolume(0.5f, 0.5f);

        mediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        if (!mediaPlayer.isPlaying()) {
//            mediaPlayer.start();
//            isPlaying = true;
//            myHandler.postDelayed(UpdateSongTime, 100);
//
//        } else {
//            mediaPlayer.pause();
//            isPlaying = false;
//        }

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//
//        if (mediaPlayer != null) {
//            mediaPlayer.release();
//        }
    }

//    public void totalTime(int time) {
//        min = time / 1000 / 60;
//        sec = time / 1000 % 60;
//        tmpTimeLabel = min + ":";
//
//        if (sec < 10) {
//            tmpTimeLabel += "0";
//        }
//        tmpTimeLabel += sec;
//        totalTimeLabel.setText(String.valueOf(tmpTimeLabel));
//    }
//
//    public void updateTimeElapsed(int time) {
//        min = time / 1000 / 60;
//        sec = time / 1000 % 60;
//        tmpUpdateTime = min + ":";
//
//        if (sec < 10) {
//            tmpUpdateTime += "0";
//        }
//        tmpUpdateTime += sec;
//        elapsedTimeLabel.setText(tmpUpdateTime);
//    }
//
//    private Runnable UpdateSongTime = new Runnable() {
//        public void run() {
//            startTime = mediaPlayer.getCurrentPosition();
//            updateTimeElapsed(mediaPlayer.getCurrentPosition());
//            songPositionBar.setProgress(mediaPlayer.getCurrentPosition());
//            myHandler.postDelayed(this, 100);
//        }
//    };
}
