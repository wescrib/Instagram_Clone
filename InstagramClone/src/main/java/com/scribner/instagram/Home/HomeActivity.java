package com.scribner.instagram.Home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scribner.instagram.Login.LoginActivity;
import com.scribner.instagram.R;
import com.scribner.util.BottomNavViewHelper;
import com.scribner.util.SectionsPagerAdapter;
import com.scribner.util.UniversalImageLoader;

public class HomeActivity extends AppCompatActivity {

    TextView currentUserTextview;
    private Context mContext = HomeActivity.this;
    private static final int ACTIVITY_NUM = 0;

    private static final String TAG = "HomeActivity";

    /******* firebase thing *********/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();

        setUpFireBaseAuth();
        initImageLoader();
        setUpBottomNavView();
        setUpViewPager();

//        mAuth.signOut();

    }

    /*********************************************FIRE BASE***************************************/

    /**
     * set up firebase authentication object
     */
    private void setUpFireBaseAuth(){
        Log.d(TAG, "setUpFireBaseAuth: setting up firebase authorization");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                checkCurrentUser(user);
                if(user != null){
                    //signed in
                    Log.d(TAG, "onAuthStateChanged: signed_in: " + user.getUid());
                }else{
                    //signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }
            }
        };
    }

    private void checkCurrentUser(FirebaseUser user){
        Log.d(TAG, "checkCurrentUser: checking if a user is logged in");
        if(user == null){
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        checkCurrentUser(mAuth.getCurrentUser());

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /******************* ADDS CAMERA, HOME, MESSAGES BUTTONS AT THE TOP**************************/
    private void setUpViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CameraFragment()); //index 0
        adapter.addFragment(new HomeFragment()); //index 1
        adapter.addFragment(new MessagesFragment()); //index 2
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
        tabLayout.getTabAt(1).setIcon(R.drawable.insta_logo);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_arrow);
    }
    
    private void setUpBottomNavView(){
        Log.d(TAG, "setUpBottomNavView: init bottom nav");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavViewHelper.setUpBottomNavView(bottomNavigationViewEx);
        BottomNavViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private void initImageLoader(){
        UniversalImageLoader imageLoader = new UniversalImageLoader(mContext);

        ImageLoader.getInstance().init(imageLoader.getConfig());
    }
}
