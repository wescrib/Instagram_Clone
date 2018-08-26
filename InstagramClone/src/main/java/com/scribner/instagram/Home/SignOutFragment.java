package com.scribner.instagram.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.scribner.instagram.Login.LoginActivity;
import com.scribner.instagram.R;

public class SignOutFragment extends Fragment {

    private static final String TAG = "SignOutFragment";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressBar mProgressBar;
    private TextView tvSignout;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(com.scribner.instagram.R.layout.fragment_signout, container, false);
        tvSignout = (TextView) view.findViewById(R.id.tvConfirmSignout);
        mProgressBar = view.findViewById(R.id.progressBar);
        Button btnConfirmSignout = view.findViewById(R.id.btnConfirmSignout);
        setUpFireBaseAuth();

        mProgressBar.setVisibility(View.GONE);

        btnConfirmSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting sign out");
                mProgressBar.setVisibility(View.VISIBLE);


                mAuth.signOut();
                //getActivity() gets the activity that the fragment is apart of. BOOM
                getActivity().finish();
            }
        });

        return view;
    }

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
                if(user != null){
                    //signed in
                    Log.d(TAG, "onAuthStateChanged: signed_in: " + user.getUid());
                }else{
                    //signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out and going back to login screen");
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);


    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
