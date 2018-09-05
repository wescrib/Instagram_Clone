package com.scribner.instagram.Share;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.scribner.instagram.R;
import com.scribner.util.BottomNavViewHelper;
import com.scribner.util.Permissions;
import com.scribner.util.SectionsPagerAdapter;

public class ShareActivity extends AppCompatActivity {

    private static final String TAG = "ShareActivity";
    private Context mContext = ShareActivity.this;
    private static final int ACTIVITY_NUM = 2;
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    private ViewPager mViewPager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Log.d(TAG, "onCreate: started");
        if(checkPermissionsArray(Permissions.PERMISSIONS)){

        }else{
            verifyPermissions(Permissions.PERMISSIONS);
        }


//        setUpBottomNavView();


    }

    /**
     * Loops through an array of permissions
     * @param permissions
     * @return
     */
    public boolean checkPermissionsArray(String[] permissions){
        Log.d(TAG, "checkPermissionsArray: checking permissions array");
        for(int i = 0; i<permissions.length; i++){
            String check = permissions[i];
            if (!checkPermissions(check)){
                return false;
            }
        }
        return true;
    }

    /**
     * Check a single permission
     * @param permission
     * @return
     */
    public boolean checkPermissions(String permission){
        Log.d(TAG, "checkPermissions: checking permission: " + permission);
        int permissionRequest = ActivityCompat.checkSelfPermission(ShareActivity.this, permission);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkPermissions: PERMISSION DENIED: " + permission);
            return false;
        }else{
            Log.d(TAG, "checkPermissions: PERMISSION GRANTED: " + permission);
            return true;
        }
    }

    /**
     * Verify all the permissions passed to the array
     * @param permissions
     */
    public void verifyPermissions(String[] permissions){
        Log.d(TAG, "verifyPermissions: verifying permissions.");
        ActivityCompat.requestPermissions(
                ShareActivity.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    private void setupViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new GalleryFragment());
//        adapter.addFragment(new PhotoFragment());

//        mViewPager = findViewById(R.id.container);
//        mViewPager.setAdapter(adapter);
//
//        TabLayout tabLayout = findViewById(R.id.tabsBottom);
//        tabLayout.setupWithViewPager(mViewPager);
//
//        tabLayout.getTabAt(0).setText(getString(R.string.gallery));
//        tabLayout.getTabAt(1).setText(getString(R.string.photo));


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
