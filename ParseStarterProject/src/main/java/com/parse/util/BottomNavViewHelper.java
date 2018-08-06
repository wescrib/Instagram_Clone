package com.parse.util;

import android.util.Log;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavViewHelper {
    private static final String TAG = "BottomNavViewHelper";

    //method that will set up the bottom nav view
    public static void setUpBottomNavView(BottomNavigationViewEx bottomNavView){
        Log.d(TAG, "setUpBottomNavView: setting up bottom nav view");
        bottomNavView.enableAnimation(false);
        bottomNavView.enableItemShiftingMode(false);
        bottomNavView.enableShiftingMode(false);
        bottomNavView.setTextVisibility(false);
    }

}
