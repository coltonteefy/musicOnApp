package com.example.coltonteefy.moapp.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coltonteefy.moapp.R;
import com.example.coltonteefy.moapp.RecyclerViewDataAdapter;
import com.example.coltonteefy.moapp.SectionDataModel;
import com.example.coltonteefy.moapp.SingleItemModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {
    ArrayList<SectionDataModel> allSampleData;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        allSampleData = new ArrayList<SectionDataModel>();

        createDummyData();

        RecyclerView home_recycler_view = view.findViewById(R.id.home_recycler_view);
        home_recycler_view.setHasFixedSize(true);
        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getContext(), allSampleData);
        home_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        home_recycler_view.setAdapter(adapter);

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
}
