package com.scribner.instagram.Share;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.scribner.instagram.R;
import com.scribner.util.FilePaths;
import com.scribner.util.FileSearch;
import com.scribner.util.GridImageAdapter;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";
    private static final int NUM_GRID_COLUMNS = 3;
    private static final String mAppend = "file:/";


    private GridView gridView;
    private ImageView galleryImage;
    private ProgressBar mPrograssbar;
    private Spinner directorySpinner;

    private ArrayList<String> directories;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        galleryImage = view.findViewById(R.id.galleryImageView);
        gridView = view.findViewById(R.id.gridView);
        directorySpinner = view.findViewById(R.id.spinnerDirectory);
        mPrograssbar = view.findViewById(R.id.progressBar);
        mPrograssbar.setVisibility(View.GONE);
        directories = new ArrayList<>();
        Log.d(TAG, "onCreateView: started");

        ImageView shareClose = view.findViewById(R.id.ivCloseShare);
        shareClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the gallery fragment");
                getActivity().finish();
            }
        });

        TextView nextScreen = view.findViewById(R.id.tvNext);
        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to final share screen");
            }
        });

        init();

        return view;
    }

    private void init(){
        FilePaths filePaths = new FilePaths();
        /**********  check for all directories holding images and jam it all into a dropdown list  ***************/
        if(FileSearch.getDirectoryPaths(filePaths.PICTURES) != null){
            //will add directors (from /Pictures directory) to *directories* array list from
            directories = FileSearch.getDirectoryPaths(filePaths.PICTURES);
        }
        directories.add(filePaths.CAMERA);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, directories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        directorySpinner.setAdapter(adapter);
        directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: selected: " + directories.get(position));
                setupGridView(directories.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setupGridView(String selectedDirectory){
        Log.d(TAG, "setupGridView: directory: " + selectedDirectory);
        final ArrayList<String> imgURLs = FileSearch.getFilePaths(selectedDirectory);

        //set grid column width
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        //use grid adapter to adapt images to gridview
        GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview, mAppend, imgURLs);
        gridView.setAdapter(adapter);

        //set first image as big displayed imageview
        setImage(imgURLs.get(0), galleryImage, mAppend);
        
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: selected image: " + imgURLs.get(position));
                setImage(imgURLs.get(position),galleryImage,mAppend);

            }
        });

    }

    private void setImage(String imageURL, ImageView image, String append){
        Log.d(TAG, "setImage: image");
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(append + imageURL, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                mPrograssbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                mPrograssbar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mPrograssbar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                mPrograssbar.setVisibility(View.INVISIBLE);

            }
        });
    }
}
