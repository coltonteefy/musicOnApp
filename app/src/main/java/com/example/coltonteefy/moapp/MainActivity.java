package com.example.coltonteefy.moapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.coltonteefy.moapp.home.HomeFragment;
import com.example.coltonteefy.moapp.play.PlayFragment;
import com.example.coltonteefy.moapp.profile.ProfileFragment;
import com.example.coltonteefy.moapp.search.SearchFragment;
import com.example.coltonteefy.moapp.upload.UploadFragment;

public class MainActivity extends AppCompatActivity {
    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new SearchFragment();
    final Fragment fragment3 = new PlayFragment();
    final Fragment fragment4 = new UploadFragment();
    final Fragment fragment5 = new ProfileFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    public void signOut(View view) {
        Intent signOut = new Intent(this, SignInActivity.class);
        startActivity(signOut);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_home:
                            fm.beginTransaction().hide(active).show(fragment1).commit();
                            active = fragment1;
                            break;
                        case R.id.action_search:
                            fm.beginTransaction().hide(active).show(fragment2).commit();
                            active = fragment2;
                            break;
                        case R.id.action_play:
                            fm.beginTransaction().hide(active).show(fragment3).commit();
                            active = fragment3;
                            break;
                        case R.id.action_upload:
                            fm.beginTransaction().hide(active).show(fragment4).commit();
                            active = fragment4;
                            break;
                        case R.id.action_profile:
                            fm.beginTransaction().hide(active).show(fragment5).commit();
                            active = fragment5;
                            break;
                    }
                    return true;
                }
            };
}
