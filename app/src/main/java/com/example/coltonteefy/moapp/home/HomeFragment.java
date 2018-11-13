package com.example.coltonteefy.moapp.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.coltonteefy.moapp.R;
import com.example.coltonteefy.moapp.RecyclerViewDataAdapter;
import com.example.coltonteefy.moapp.SectionDataModel;
import com.example.coltonteefy.moapp.SingleItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    private final String TAG = "HomeFragment";
    RecyclerView home_recycler_view;
    RecyclerViewDataAdapter adapter;
    ArrayList<SectionDataModel> allSampleData = new ArrayList<SectionDataModel>();
    List<String> musicGenres = Arrays.asList(
            "Alternative",
            "Blues",
            "Classical",
            "Country",
            "Dance",
            "Electronic",
            "Hip Hop/Rap",
            "Experimental",
            "Jazz",
            "Indie",
            "R&B/Soul",
            "Rock",
            "Metal",
            "Reggae"
    );

    ArrayList<String> userNames = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
//
//        allSampleData = new ArrayList<SectionDataModel>();
//        usernames = new ArrayList<String>();

        createDummyData();
//        new OkHttpAsync().execute();

        home_recycler_view = view.findViewById(R.id.home_recycler_view);
        home_recycler_view.setHasFixedSize(true);
        adapter = new RecyclerViewDataAdapter(getContext(), allSampleData);
        home_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        home_recycler_view.setAdapter(adapter);


        LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(broadcastReceiver, new IntentFilter("users"));

        return view;
    }


    public void createDummyData() {
        for (int i = 0; i <= musicGenres.size() - 1; i++) {

            SectionDataModel dm = new SectionDataModel();

            dm.setHeaderTitle(musicGenres.get(i));

            ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
            for (int j = 0; j <= 10; j++) {
                singleItem.add(new SingleItemModel("Artist" + j, "Song Name", "URL " + j));
            }

            dm.setAllItemsInSection(singleItem);

            allSampleData.add(dm);

        }
    }


//    class OkHttpAsync extends AsyncTask<Object, Void, Object> {
//
//        @Override
//        protected Object doInBackground(Object... objects) {
//            String url = "https://afternoon-waters-54974.herokuapp.com/getAllMusic";
//            OkHttpClient client = new OkHttpClient();
//            Request request = new Request.Builder()
//                    .url(url)
//                    .build();
//
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    try {
//                        JSONArray jsonArray = new JSONArray(response.body().string());
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            userNames.add(jsonObject.get("username").toString());
////                            Log.d(TAG, "\n" + jsonObject.get("username") + " " + jsonObject.get("userMusic"));
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object o) {
//            super.onPostExecute(o);
////            Log.d(TAG, "USERNAMES " + usernames.size());
//
//            for(int i = 0; i < userNames.size(); i++) {
//                Log.d(TAG, "USERNAMES " + userNames.get(i));
//            }
//
//        }
//    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            userNames = intent.getStringArrayListExtra("eachUser");
            Log.d(TAG, "HomeFragReceive: " + userNames);

            if(userNames.size() > 0) {
                for (int i = 0; i <= userNames.size() - 1; i++) {

                    SectionDataModel dm = new SectionDataModel();

                    dm.setHeaderTitle(userNames.get(i));
                    Log.d(TAG, "U " + userNames.get(i));

                    ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                    for (int j = 0; j <= 2; j++) {
                        singleItem.add(new SingleItemModel("Artist" + j, "Song Name", "URL " + j));
                        Log.d(TAG, "item " + singleItem.get(j));
                    }
                    dm.setAllItemsInSection(singleItem);
                    allSampleData.add(dm);
                }
            }
        }
    };
}
