package com.scribner.instagram.Share;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.scribner.instagram.R;

public class NextActivity extends AppCompatActivity{
    private static final String TAG = "NextActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        Log.d(TAG, "onCreate: get the chosen image " + getIntent().getStringExtra(getString(R.string.selected_image)));
    }
}
