package com.example.coltonteefy.moapp.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coltonteefy.moapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchFragment extends Fragment {

    public static final String TAG = "SearchFragment";
    EditText edtSearchText;
    ImageView clearText;

    private ArrayList<String> mID = new ArrayList<>();
    private ArrayList<String> mArtist = new ArrayList<>();
    private ArrayList<String> mTitle = new ArrayList<>();
    private ArrayList<String> mGenre = new ArrayList<>();
    private ArrayList<String> mcoverArtUrl = new ArrayList<>();
    private ArrayList<String> msongUrl = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        clearText = view.findViewById(R.id.clear_text);

        edtSearchText = view.findViewById(R.id.edt_search_text);

        edtSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return actionId == EditorInfo.IME_ACTION_SEARCH;
            }
        });


        TextWatcher searchTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                clearSearch();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    clearText.setVisibility(View.GONE);
                    clearSearch();
                } else {
                    clearText.setVisibility(View.VISIBLE);

                    String searchQuery = edtSearchText.getText().toString();
//                    String searchQuery = null;
//                    try {
//                        searchQuery = URLEncoder.encode(edtSearchText.getText().toString(), "UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }

                    String URL = "https://afternoon-waters-54974.herokuapp.com/searchMusic?term=" + searchQuery;

                    OkHttpClient client = new OkHttpClient();
                    final Request request = new Request.Builder()
                            .url(URL)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String message = response.body().string();
//                            Log.i(TAG, "onTextChanged: " + message);

                            try {
                                JSONArray jsonArray = new JSONArray(message);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.i(TAG, "onResponse: NEW " + i + "\n"
                                            + jsonObject.get("id") + "\n"
                                            + jsonObject.get("artist") + "\n"
                                            + jsonObject.get("title") + "\n"
                                            + jsonObject.get("genre") + "\n"
                                            + jsonObject.get("songUrl") + "\n"
                                            + jsonObject.get("coverArtUrl") + "\n");

                                    mID.add(jsonObject.get("id").toString());
                                    mArtist.add(jsonObject.get("artist").toString());
                                    mTitle.add(jsonObject.get("title").toString());
                                    mGenre.add(jsonObject.get("genre").toString());
                                    msongUrl.add(jsonObject.get("songUrl").toString());
                                    mcoverArtUrl.add(jsonObject.get("coverArtUrl").toString());
                                }

                                initRecyclerView();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearchText.getText().clear();
            }
        });

        edtSearchText.addTextChangedListener(searchTextWatcher);


        return view;
    }

    private void initRecyclerView() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RecyclerView recyclerView = getActivity().findViewById(R.id.search_recycler_view);
                SearchRecylerViewAdapater adapter = new SearchRecylerViewAdapater(mID, mArtist, mTitle, mGenre, mcoverArtUrl, msongUrl, getContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
            }
        });
    }

    public void clearSearch() {
        mID.clear();
        mArtist.clear();
        mTitle.clear();
        mGenre.clear();
        msongUrl.clear();
        mcoverArtUrl.clear();
        initRecyclerView();
    }
}
