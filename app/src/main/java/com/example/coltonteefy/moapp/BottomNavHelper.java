package com.example.coltonteefy.moapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class BottomNavHelper {

    //  navigate home transition
    public static void enterRightLeaveLeft(Fragment activeFragment, Fragment nextFragment, FragmentManager fm) {
        fm.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                .hide(activeFragment)
                .show(nextFragment)
                .commit();
    }

    //  navigate profile transition
    public static void enterLeftLeaveRight(Fragment activeFragment, Fragment nextFragment, FragmentManager fm) {
        fm.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                .hide(activeFragment)
                .show(nextFragment)
                .commit();
    }

    //  navigate middle items transition
    public static void fragmentTransition(Fragment activeFragment, Fragment nextFragment, FragmentManager fm, int activePos, int futurePos) {
        if (activePos < futurePos) {
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                    .hide(activeFragment)
                    .show(nextFragment)
                    .commit();
        } else {
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                    .hide(activeFragment)
                    .show(nextFragment)
                    .commit();
        }
    }
}
