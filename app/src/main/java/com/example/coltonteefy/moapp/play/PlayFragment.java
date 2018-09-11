package com.example.coltonteefy.moapp.play;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coltonteefy.moapp.R;

public class PlayFragment extends Fragment {

    Button playBtn, rewind, fastForward;
    SeekBar songPositionBar;
    TextView elapsedTimeLabel, totalTimeLabel;
    MediaPlayer mp;
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

        //  media player
        mp = MediaPlayer.create(getActivity(), R.raw.boris_brejcha_purple_noise_id0001);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);

        totalTime(mp.getDuration());
        songPositionBar.setMax(mp.getDuration());

        //  play/pause controller
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.setLooping(true);
                if (!mp.isPlaying()) {
                    mp.start();
                    playBtn.setBackgroundResource(R.drawable.ic_pause_button);
                    isPlaying = true;
                    myHandler.postDelayed(UpdateSongTime, 100);
                } else {
                    mp.pause();
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
                    mp.seekTo(progress);
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

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Toast.makeText(getActivity(), "DESTROYED", Toast.LENGTH_SHORT).show();
        mp.stop();
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
        tmpUpdateTime = min + ":";

        if (sec < 10) {
            tmpUpdateTime += "0";
        }
        tmpUpdateTime += sec;
        elapsedTimeLabel.setText(tmpUpdateTime);
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mp.getCurrentPosition();
            updateTimeElapsed(mp.getCurrentPosition());
            songPositionBar.setProgress(mp.getCurrentPosition());
            myHandler.postDelayed(this, 100);
        }
    };
}
