package com.example.coltonteefy.moapp.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.coltonteefy.moapp.BottomNavHelper;
import com.example.coltonteefy.moapp.R;
import com.example.coltonteefy.moapp.RecyclerViewDataAdapter;
import com.example.coltonteefy.moapp.SectionDataModel;
import com.example.coltonteefy.moapp.SingleItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    LinearLayout localEvents, addEvents;

    private ArrayList<String> mID = new ArrayList<>();
    private ArrayList<String> mArtist = new ArrayList<>();
    private ArrayList<String> mTitle = new ArrayList<>();
    private ArrayList<String> mGenre = new ArrayList<>();
    private ArrayList<String> mcoverArtUrl = new ArrayList<>();
    private ArrayList<String> msongUrl = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        getAllUserMusic();

        localEvents = view.findViewById(R.id.local_events);
        addEvents = view.findViewById(R.id.add_events);

        home_recycler_view = view.findViewById(R.id.home_recycler_view);
        home_recycler_view.setHasFixedSize(true);
        adapter = new RecyclerViewDataAdapter(getContext(), allSampleData);
        home_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        home_recycler_view.setAdapter(adapter);


        //  bottom navigation
        BottomNavigationView bottomNav = view.findViewById(R.id.top_navigation);
        bottomNav.setOnNavigationItemSelectedListener(topNavListener);
        return view;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener topNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_music:
                            home_recycler_view.setVisibility(View.VISIBLE);
                            localEvents.setVisibility(View.GONE);
                            addEvents.setVisibility(View.GONE);
                            break;
                        case R.id.action_local_events:
                            home_recycler_view.setVisibility(View.GONE);
                            localEvents.setVisibility(View.VISIBLE);
                            addEvents.setVisibility(View.GONE);
                            break;
                        case R.id.action_add_event:
                            home_recycler_view.setVisibility(View.GONE);
                            localEvents.setVisibility(View.GONE);
                            addEvents.setVisibility(View.VISIBLE);
                            break;
                    }
                    return true;
                }
            };

    public void getAllUserMusic() {

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
                        JSONArray array = (JSONArray) jsonObject.get("userMusic");

                        for(int j = 0; j < array.length(); j++) {
                            JSONObject o = array.getJSONObject(j);

                            mID.add(o.get("id").toString());
                            mArtist.add(o.get("artist").toString());
                            mTitle.add(o.get("title").toString());
                            mGenre.add(o.get("genre").toString());
                            msongUrl.add(o.get("songUrl").toString());
                            mcoverArtUrl.add(o.get("coverArtUrl").toString());
                        }
                    }

                    initRecyclerView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initRecyclerView() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RecyclerView recyclerView = getActivity().findViewById(R.id.home_recycler_view);
                HomeRecylerViewAdapater adapter = new HomeRecylerViewAdapater(mID, mArtist, mTitle, mGenre, mcoverArtUrl, msongUrl, getContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
            }
        });
    }
}
