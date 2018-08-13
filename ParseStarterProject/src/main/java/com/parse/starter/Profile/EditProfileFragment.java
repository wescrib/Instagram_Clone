package com.parse.starter.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.starter.R;
import com.parse.util.UniversalImageLoader;

public class EditProfileFragment extends Fragment {

    private static final String TAG = "EditProfileFragment";

    private ImageView mProfilePhoto;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);
        mProfilePhoto = view.findViewById(R.id.profile_photo);
        initImageLoader();
        setProfileImage();


        //back button
        ImageView backArrow = view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to profile");
                getActivity().finish();
            }
        });

        return view;
    }

    private void initImageLoader(){
        UniversalImageLoader imageLoader = new UniversalImageLoader(getActivity());

        ImageLoader.getInstance().init(imageLoader.getConfig());
    }

    private void setProfileImage(){
        Log.d(TAG, "setProfileImage: setting profile image");
        String imgURL = "http://getdrawings.com/img/generic-silhouette-10.jpg";

        UniversalImageLoader.setImage(imgURL, mProfilePhoto, null,"");

    }
}
