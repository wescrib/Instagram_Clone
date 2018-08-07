package com.parse.util;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.parse.starter.Home.HomeActivity;
import com.parse.starter.Likes.LikesActivity;
import com.parse.starter.Profile.ProfileActivity;
import com.parse.starter.R;
import com.parse.starter.Search.SearchActivity;
import com.parse.starter.Share.ShareActivity;

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

    public static void enableNavigation(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.ic_house:
                        Intent intent1 = new Intent(context, HomeActivity.class); // 0
                        context.startActivity(intent1);
                        break;

                    case R.id.ic_search:
                        Intent intent2 = new Intent(context, SearchActivity.class); //1
                        context.startActivity(intent2);
                        break;

                    case R.id.ic_circle:
                        Intent intent3 = new Intent(context, ShareActivity.class); //2
                        context.startActivity(intent3);
                        break;

                    case R.id.ic_alert:
                        Intent intent4 = new Intent(context, LikesActivity.class); //3
                        context.startActivity(intent4);
                        break;

                    case R.id.ic_person:
                        Intent intent5 = new Intent(context, ProfileActivity.class); //4
                        context.startActivity(intent5);
                        break;
                }
                return false;
            }
        });
    }

}
