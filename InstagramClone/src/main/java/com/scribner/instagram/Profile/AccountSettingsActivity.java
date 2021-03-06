package com.scribner.instagram.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.scribner.instagram.Home.SignOutFragment;
import com.scribner.instagram.R;
import com.scribner.util.BottomNavViewHelper;
import com.scribner.util.SectionStatePagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class AccountSettingsActivity extends AppCompatActivity{

    private static final String TAG = "AccountSettingsActivity";
    private static final int ACTIVITY_NUM = 4;
    private Context mContext = AccountSettingsActivity.this;
    private SectionStatePagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsettings);
        Log.d(TAG, "onCreate: started");

        mViewPager = (ViewPager) findViewById(R.id.container);
        mRelativeLayout = findViewById(R.id.relLayout1);

        setUpSettingsList();
        SetUpFragments();
        setUpBottomNavView();
        getIncomingIntent();

        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to profile");
                finish();
            }
        });
    }
    
    private void getIncomingIntent(){
        Intent intent = getIntent();
        //if the incoming intent has a string (calling activity) then it will go directly to the edit profile fragment
        if(intent.hasExtra(getString(R.string.calling_activity))){
            Log.d(TAG, "getIncomingIntent: received incoming intent from " + getString(R.string.profile_activity));
            setViewPager(pagerAdapter.getFragmentNumber(getString(R.string.edit_profile_fragment)));
        }
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

    private void setUpSettingsList(){
        Log.d(TAG, "setUpSettingsList: starting account settings list");
        ListView listView = findViewById(R.id.lvAccountSettings);
        ArrayList<String> options = new ArrayList<>(Arrays.asList(getString(R.string.edit_profile_fragment), getString(R.string.sign_out_fragment)));

        ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, options);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: navigating to fragment: " + position);
                setViewPager(position); //position in the listview
            }
        });
    }

    private void SetUpFragments(){
        pagerAdapter = new SectionStatePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new EditProfileFragment(), getString(R.string.edit_profile_fragment)); //fragment 0
        pagerAdapter.addFragment(new SignOutFragment(), getString(R.string.sign_out_fragment)); //fragment 1
    }

    private void setViewPager(int fragNum){
        mRelativeLayout.setVisibility(View.GONE);
        Log.d(TAG, "setViewPager: navigating to frag number" + fragNum);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(fragNum);
    }
}
