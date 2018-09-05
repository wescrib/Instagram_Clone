package com.scribner.instagram.Share;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.scribner.instagram.R;
import com.scribner.util.Permissions;

public class PhotoFragment extends Fragment {
    private static final String TAG = "PhotoFragment";

    private static final int PHOTO_FRAGMENT_NUMBER = 1;
    private static final int GALLERY_FRAGMENT_NUMBER = 2;
    private static final int CAMERA_REQUEST_CODE = 5;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        Log.d(TAG, "onCreateView: started");

        Button btnLaunchCamera = view.findViewById(R.id.buttonLaunCamera);
        
        btnLaunchCamera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: launching camera");
                if(((ShareActivity)getActivity()).getCurruentTabNumber()== PHOTO_FRAGMENT_NUMBER){
                    if(((ShareActivity)getActivity()).checkPermissions(Permissions.CAMERA_PERMISSION[0])){
                        Log.d(TAG, "onClick: starting camera");

                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                    }else{
                        Intent intent = new Intent(getActivity(), ShareActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST_CODE){
            Log.d(TAG, "onActivityResult: finished taking photo");
            Log.d(TAG, "onActivityResult: attempting to navigate to final share screen");

        }

    }
}
