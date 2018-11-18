package com.example.coltonteefy.moapp.play;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coltonteefy.moapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class PlayFragment extends Fragment {

    private final String TAG = "PlayFragment";
    Button playBtn, previousBtn, nextBtn;
    SeekBar songPositionBar;
    TextView elapsedTimeLabel, totalTimeLabel, artistName, songName;
    ImageView imageCoverArt;
    MediaPlayer mediaPlayer;
    String tmpTimeLabel, tmpUpdateTime;
    Boolean isPlaying = false;
    int min, sec;
    double startTime;
    private Handler myHandler = new Handler();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play, container, false);

        playBtn = view.findViewById(R.id.playBtn);
        totalTimeLabel = view.findViewById(R.id.totalTimeLabel);
        elapsedTimeLabel = view.findViewById(R.id.elapsedTimeLabel);
        songPositionBar = view.findViewById(R.id.songPositionBar);

        artistName = view.findViewById(R.id.artistName);
        songName = view.findViewById(R.id.songName);
        imageCoverArt = view.findViewById(R.id.imageCoverArt);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter("updateTime"));

//        myHandler.postDelayed(UpdateSongTime, 100);

        //  play/pause controller
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying) {
                    playBtn.setBackgroundResource(R.drawable.ic_pause_button);
                    isPlaying = true;
                } else {
                    playBtn.setBackgroundResource(R.drawable.ic_play_button);
                    isPlaying = false;
                }
            }
        });

        //  song position
        songPositionBar = view.findViewById(R.id.songPositionBar);
        songPositionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    songPositionBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        nextBtn = view.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "NEXT SONG", Toast.LENGTH_SHORT).show();
            }
        });


        previousBtn = view.findViewById(R.id.previousBtn);
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "PREVIOUS SONG", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Toast.makeText(getActivity(), "DESTROYED", Toast.LENGTH_SHORT).show();
        mediaPlayer.stop();
    }


    public void totalTime(int time) {
        min = time / 1000 / 60;
        sec = time / 1000 % 60;
        tmpTimeLabel = min + ":";

        if (sec < 10) {
            tmpTimeLabel += "0";
        }
        tmpTimeLabel += sec;
        totalTimeLabel.setText(String.valueOf(tmpTimeLabel));
    }

    public void updateTimeElapsed(int time) {
        min = time / 1000 / 60;
        sec = time / 1000 % 60;
        tmpUpdateTime = min + getString(R.string.colon);

        if (sec < 10) {
            tmpUpdateTime += getString(R.string.zero);
        }
        tmpUpdateTime += sec;
        elapsedTimeLabel.setText(tmpUpdateTime);
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            updateTimeElapsed(mediaPlayer.getCurrentPosition());
            songPositionBar.setProgress(mediaPlayer.getCurrentPosition());
            myHandler.postDelayed(this, 100);
        }
    };

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            elapsedTimeLabel.setText(intent.getStringExtra("elapsedTime"));

            Log.i(TAG, "onReceive: TOTAL TIME " + intent.getStringExtra("artist"));

            int time = Integer.parseInt(intent.getStringExtra("totalTime"));
            totalTime(time);

            artistName.setText(intent.getStringExtra("artist"));
            songName.setText(intent.getStringExtra("songTitle"));

            Picasso.get()
                    .load(intent.getStringExtra("coverArtUrl"))
                    .resize(100, 100)
                    .centerCrop()
                    .into(imageCoverArt);

            playBtn.setBackgroundResource(R.drawable.ic_pause_button);
            isPlaying = true;
        }
    };


}
