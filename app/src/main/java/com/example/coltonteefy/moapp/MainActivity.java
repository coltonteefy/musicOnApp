package com.example.coltonteefy.moapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coltonteefy.moapp.home.HomeFragment;
import com.example.coltonteefy.moapp.play.PlayFragment;
import com.example.coltonteefy.moapp.profile.ProfileFragment;
import com.example.coltonteefy.moapp.search.SearchFragment;
import com.example.coltonteefy.moapp.upload.UploadFragment;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Starting");

        //  bottom navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //  create fragment instances in order to maintain states when navigating between each
        fm.beginTransaction().add(R.id.fragment_container, fragment5, "5").hide(fragment5).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment1, "1").commit();


        new MainActivity.OkHttpAsync().execute();

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
    }


    class OkHttpAsync extends AsyncTask<Object, Void, Object> {

        @Override
        protected Object doInBackground(Object... objects) {
            String url = "https://afternoon-waters-54974.herokuapp.com/getAllMusic";
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            userList.add(jsonObject.get("username").toString());
//                            Log.d(TAG, "\n" + jsonObject.get("username") + " " + jsonObject.get("userMusic"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
//            Log.d(TAG, "USERNAMES " + usernames.size());
            for (int i = 0; i < userList.size(); i++) {
                Log.d(TAG, "USERNAMES " + userList.get(i));
            }

            Intent intent = new Intent();
            intent.setAction("users");
            intent.putExtra("eachUser", userList);
            LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent);

            Log.d(TAG, "USER SIZE " + userList.size());

        }
    }
}
