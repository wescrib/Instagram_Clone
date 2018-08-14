package com.parse.starter.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;


import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.parse.starter.R;
import com.parse.util.BottomNavViewHelper;
import com.parse.util.GridImageAdapter;
import com.parse.util.UniversalImageLoader;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private Context mContext = ProfileActivity.this;
    private static final int ACTIVITY_NUM = 4;
    private ProgressBar mProgressBar;
    private ImageView profilePhoto;

    private static final int NUM_GRID_COLUMNS = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: started");
        mProgressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
        mProgressBar.setVisibility(View.GONE);
        setUpBottomNavView();
        setUpToolBar();
        setUpActivityWidgets();
        setProfilePhoto();
        tempGridSetup();

    }

    private void tempGridSetup(){
        ArrayList<String> imgURLs = new ArrayList<>();

        imgURLs.add("https://img.maximummedia.ie/joe_co_uk/eyJkYXRhIjoie1widXJsXCI6XCJodHRwOlxcXC9cXFwvbWVkaWEtam9lY291ay5tYXhpbXVtbWVkaWEuaWUuczMuYW1hem9uYXdzLmNvbVxcXC93cC1jb250ZW50XFxcL3VwbG9hZHNcXFwvMjAxN1xcXC8wN1xcXC8xMjA5MjY1NlxcXC9uaWMtY2FnZS0xMDI0eDUzOC5qcGdcIixcIndpZHRoXCI6NzY3LFwiaGVpZ2h0XCI6NDMxLFwiZGVmYXVsdFwiOlwiaHR0cHM6XFxcL1xcXC93d3cuam9lLmNvLnVrXFxcL2Fzc2V0c1xcXC9pbWFnZXNcXFwvam9lY291a1xcXC9uby1pbWFnZS5wbmc_dj01XCJ9IiwiaGFzaCI6IjM4MTU5ODM3NDQzM2I4YWY3MmZhZjM5YzFjYjhjMTc3YTM3OTllMDUifQ==/nic-cage-1024x538.jpg");
        imgURLs.add("https://consequenceofsound.files.wordpress.com/2018/03/nicolas-cage-superman-teen-titans-animated.png?w=807");
        imgURLs.add("https://sadanduseless.b-cdn.net/wp-content/uploads/2012/08/n1.jpg");
        imgURLs.add("https://i.kinja-img.com/gawker-media/image/upload/s--y0OM6BOQ--/c_scale,f_auto,fl_progressive,q_80,w_800/iwpzjy3ggdpapoagr8av.jpg");
        imgURLs.add("https://www.aceshowbiz.com/images/photo/nicolas_cage.jpg");
        imgURLs.add("https://media1.popsugar-assets.com/files/thumbor/g0cDuWhsdD3sbdx5ajNPTXF-mrA/fit-in/550x550/filters:format_auto-!!-:strip_icc-!!-/2014/01/06/008/n/1922283/131b4126c7b8738f_thumb_temp_image333458751389045360/i/Best-Nicolas-Cage-Memes.jpg");
        imgURLs.add("https://static1.squarespace.com/static/51b3dc8ee4b051b96ceb10de/t/58e53d6617bffc2041f4795c/1491418481293/?format=2500w");
        imgURLs.add("https://serpenturbanclothing.com/wp-content/uploads/2017/08/23413268161_9997818497-1.jpg");
        imgURLs.add("https://www.famousbirthdays.com/headshots/nicolas-cage-8.jpg");
        imgURLs.add("http://farm3.staticflickr.com/2884/11398249173_b56c77bfc0_b.jpg");
        imgURLs.add("http://3.bp.blogspot.com/_pfnegU3uTGQ/Sk2GGagqFbI/AAAAAAAAABI/OvpVqbSEZhs/S254/nicolascage.jpg");
        imgURLs.add("https://i.kym-cdn.com/entries/icons/facebook/000/002/910/not-the-bees.jpg");
        imgURLs.add("https://i.pinimg.com/236x/87/4e/50/874e50398d8f024992c1a8ce396f2ccf--nicolas-cage-images-with-quotes.jpg");

        setUpImageGrid(imgURLs);
    }

    private void setUpImageGrid(ArrayList<String> imgURLs){
        GridView gridView = findViewById(R.id.gridView);
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);
        GridImageAdapter adapter = new GridImageAdapter(mContext, R.layout.layout_grid_imageview, "", imgURLs);
        gridView.setAdapter(adapter);
    }

    private void setProfilePhoto(){
        Log.d(TAG, "setProfilePhoto: setting profile photo");

        String imgURL = "http://getdrawings.com/img/generic-silhouette-10.jpg";
        UniversalImageLoader.setImage(imgURL, profilePhoto,mProgressBar,"");
    }

    private void setUpActivityWidgets(){
        mProgressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
        mProgressBar.setVisibility(View.GONE);
        profilePhoto = findViewById(R.id.profile_photo);
    }

    private void setUpToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);

        ImageView profileMenu = (ImageView) findViewById(R.id.profileMenu);

        profileMenu.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to account settings");
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    /******** BOTTOM NAV VIEW SETUP ***********/
    private void setUpBottomNavView(){
        Log.d(TAG, "setUpBottomNavView: init bottom nav");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavViewHelper.setUpBottomNavView(bottomNavigationViewEx);
        BottomNavViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
