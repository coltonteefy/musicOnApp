package com.example.coltonteefy.moapp.search;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coltonteefy.moapp.R;

public class SearchFragment extends Fragment {

    EditText edtSearchText;
    ImageView clearText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.fragment_search, container,false);

        clearText = view.findViewById(R.id.clear_text);

        edtSearchText = view.findViewById(R.id.edt_search_text);

        edtSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    performSearch();
                    return true;
                }
                return false;
            }
        });


        TextWatcher searchTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    clearText.setVisibility(View.GONE);
                } else {
                    clearText.setVisibility(View.VISIBLE);
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
}
