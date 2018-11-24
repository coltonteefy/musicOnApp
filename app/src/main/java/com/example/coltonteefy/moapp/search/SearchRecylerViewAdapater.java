package com.example.coltonteefy.moapp.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.coltonteefy.moapp.MainActivity;
import com.example.coltonteefy.moapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchRecylerViewAdapater extends RecyclerView.Adapter<SearchRecylerViewAdapater.ViewHolder> {
    private final String TAG = "SearchRecylerViewAdap";

    private ArrayList<String> mID = new ArrayList<>();
    private ArrayList<String> mArtist = new ArrayList<>();
    private ArrayList<String> mTitle = new ArrayList<>();
    private ArrayList<String> mGenre = new ArrayList<>();
    private ArrayList<String> mCoverArtUrl = new ArrayList<>();
    private ArrayList<String> msongUrl = new ArrayList<>();
    private Context mContext;

    SearchRecylerViewAdapater(ArrayList<String> mID, ArrayList<String> mArtist, ArrayList<String> mTitle, ArrayList<String> mGenre, ArrayList<String> mcoverArtUrl, ArrayList<String> msongUrl, Context mContext) {
        this.mID = mID;
        this.mArtist = mArtist;
        this.mTitle = mTitle;
        this.mGenre = mGenre;
        this.mCoverArtUrl = mcoverArtUrl;
        this.msongUrl = msongUrl;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_search_songs, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.ID.setText(mID.get(i));
        viewHolder.artist.setText(mArtist.get(i));
        viewHolder.songTitle.setText(mTitle.get(i));
        viewHolder.genre.setText(mGenre.get(i));
        viewHolder.songUrl.setText(msongUrl.get(i));
        viewHolder.coverArtUrl.setText(mCoverArtUrl.get(i));

        Picasso.get()
                .load(mCoverArtUrl.get(i))
                .resize(500, 500)
                .centerCrop()
                .into(viewHolder.imageView);

//        Log.i(TAG, "onBindViewHolder: \n" + i  + "\n"
//                + mID.get(i) + "\n"
//                + mArtist.get(i) + "\n"
//                + mTitle.get(i) + "\n"
//                + mGenre.get(i) + "\n"
//                + msongUrl.get(i) + "\n"
//                + mCoverArtUrl.get(i) + "\n");

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) mContext;
                mainActivity.moveToPlayFragment(viewHolder.songUrl.getText().toString(), viewHolder.coverArtUrl.getText().toString(), viewHolder.artist.getText().toString(), viewHolder.songTitle.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {

        return mArtist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView artist, songTitle, songUrl, ID, coverArtUrl, genre;
        LinearLayout parentLayout;
        ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ID = itemView.findViewById(R.id.search_ID);
            artist = itemView.findViewById(R.id.search_artist);
            songTitle = itemView.findViewById(R.id.search_song_title);
            genre = itemView.findViewById(R.id.search_genre);
            imageView = itemView.findViewById(R.id.search_imageView);
            songUrl = itemView.findViewById(R.id.search_songUrl);
            coverArtUrl = itemView.findViewById(R.id.search_CoverArtUrl);
            parentLayout = itemView.findViewById(R.id.search_parentLayout);

        }
    }
}