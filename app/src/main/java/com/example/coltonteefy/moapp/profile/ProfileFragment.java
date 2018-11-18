package com.example.coltonteefy.moapp.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.coltonteefy.moapp.MainActivity;
import com.example.coltonteefy.moapp.R;
import com.example.coltonteefy.moapp.SignInActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileFragment extends Fragment {
    private final String TAG = "ProfileFragment";
    Toolbar mToolBar;
    String username, imageURL;
    TextView userTextView;
    CircleImageView profileImage;

    private ArrayList<String> mID = new ArrayList<>();
    private ArrayList<String> mArtist = new ArrayList<>();
    private ArrayList<String> mTitle = new ArrayList<>();
    private ArrayList<String> mGenre = new ArrayList<>();
    private ArrayList<String> mcoverArtUrl = new ArrayList<>();
    private ArrayList<String> msongUrl = new ArrayList<>();

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Log.d(TAG, "onCreateView: ProfileFragment started");

        profileImage = view.findViewById(R.id.profile_image);
        recyclerView = view.findViewById(R.id.profileRecyclerView);

        username = getActivity().getIntent().getStringExtra("username");
        userTextView = view.findViewById(R.id.profile_username);
        userTextView.setText(username);

        getUserImage();
        getUserMusic();

        mToolBar = view.findViewById(R.id.dropMenuBtn);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolBar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        setHasOptionsMenu(true);

        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_edit_profile:
                        Toast.makeText(getActivity(), "Edit profile", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_sign_out:
                        Intent signOut = new Intent(getActivity(), SignInActivity.class);
                        startActivity(signOut);
                        break;
                }
                return true;
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void getUserImage() {
        String URL = "https://afternoon-waters-54974.herokuapp.com/getUserImage/";
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL + username)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        imageURL = jsonObject.get("message").toString();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Picasso.get()
                                    .load(imageURL)
                                    .resize(100, 100)
                                    .centerCrop()
                                    .into(profileImage);
                        }
                    });
                }
            }
        });
    }

    public void getUserMusic() {
        String URL = "https://afternoon-waters-54974.herokuapp.com/getSingleUserMusic/";
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL + username)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = (JSONArray) jsonObject.get("music");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject o = jsonArray.getJSONObject(i);

                        mID.add(o.get("id").toString());
                        mArtist.add(username);
                        mTitle.add(o.get("title").toString());
                        mGenre.add(o.get("genre").toString());
                        msongUrl.add(o.get("songUrl").toString());
                        mcoverArtUrl.add(o.get("coverArtUrl").toString());
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
                RecyclerView recyclerView = getActivity().findViewById(R.id.profileRecyclerView);
                ProfileRecylerViewAdapater adapter = new ProfileRecylerViewAdapater(mID, mArtist, mTitle, mGenre, mcoverArtUrl, msongUrl, getContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
            }
        });
    }
}
