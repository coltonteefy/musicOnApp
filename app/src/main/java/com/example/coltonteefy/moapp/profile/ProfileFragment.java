package com.example.coltonteefy.moapp.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.coltonteefy.moapp.R;
import com.example.coltonteefy.moapp.SignInActivity;

public class ProfileFragment extends Fragment {
    Toolbar mToolBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mToolBar = view.findViewById(R.id.dropMenuBtn);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolBar);
        setHasOptionsMenu(true);

        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
//                Toast.makeText(getActivity(), "" + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
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

    //                        Intent signOut = new Intent(getActivity(), SignInActivity.class);
//                        startActivity(signOut);
}
