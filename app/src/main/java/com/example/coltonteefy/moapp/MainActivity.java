package com.example.coltonteefy.moapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.coltonteefy.moapp.home.HomeFragment;
import com.example.coltonteefy.moapp.play.PlayFragment;
import com.example.coltonteefy.moapp.play.PlayMusicService;
import com.example.coltonteefy.moapp.profile.ProfileFragment;
import com.example.coltonteefy.moapp.search.SearchFragment;
import com.example.coltonteefy.moapp.upload.UploadFragment;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new SearchFragment();
    final Fragment fragment3 = new PlayFragment();
    final Fragment fragment4 = new UploadFragment();
    final Fragment fragment5 = new ProfileFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;
    int activePosition = 1, futurePosition = 2;
    ArrayList<String> userList = new ArrayList<>();
    String username, songUrl, coverArtUrl, artist, songTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: MainActivity Starting");

        //  bottom navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //  create fragment instances in order to maintain states when navigating between each
        fm.beginTransaction().add(R.id.fragment_container, fragment5, "5").hide(fragment5).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment1, "1").commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_home:
                            if (activePosition != 1) {
                                BottomNavHelper.enterRightLeaveLeft(active, fragment1, fm);
                                active = fragment1;
                                activePosition = 1;
                            }
                            break;
                        case R.id.action_search:
                            if (activePosition != 2) {
                                futurePosition = 2;
                                BottomNavHelper.fragmentTransition(active, fragment2, fm, activePosition, futurePosition);
                                active = fragment2;
                                activePosition = 2;
                            }
                            break;
                        case R.id.action_play:
                            if (activePosition != 3) {
                                futurePosition = 3;
                                BottomNavHelper.fragmentTransition(active, fragment3, fm, activePosition, futurePosition);
                                active = fragment3;
                                activePosition = 3;
                            }
                            break;
                        case R.id.action_upload:
                            if (activePosition != 4) {
                                futurePosition = 4;
                                BottomNavHelper.fragmentTransition(active, fragment4, fm, activePosition, futurePosition);
                                active = fragment4;
                                activePosition = 4;
                            }
                            break;
                        case R.id.action_profile:
                            if (activePosition != 5) {
                                BottomNavHelper.enterLeftLeaveRight(active, fragment5, fm);
                                active = fragment5;
                                activePosition = 5;
                            }
                            break;
                    }
                    return true;
                }
            };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            Log.d(TAG, "onActivityResult: File.. " + filePath);

            Intent intent = new Intent();
            intent.setAction("file");
            intent.putExtra("filePath", filePath);
            LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent);
        }

        username = data.getStringExtra("username");
        Log.i(TAG, "MAIN ACTIVITY USERNAME: " + username);
    }

    public void moveToPlayFragment(String songUrl, String coverArtUrl, String artist, String songTitle) {
        futurePosition = 3;
        BottomNavHelper.fragmentTransition(active, fragment3, fm, activePosition, futurePosition);
        active = fragment3;
        activePosition = 3;

        Intent intent = new Intent(MainActivity.this, PlayMusicService.class);
        intent.putExtra("songUrl", songUrl);
        intent.putExtra("coverArtUrl", coverArtUrl);
        intent.putExtra("artist", artist);
        intent.putExtra("songTitle", songTitle);

        stopService(intent);
        startService(intent);
    }



}
