package com.scribner.instagram.Profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.CrashUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.scribner.instagram.Models.User;
import com.scribner.instagram.Models.UserAccountSettings;
import com.scribner.instagram.Models.UserSettings;
import com.scribner.instagram.R;
import com.scribner.util.FirebaseMethods;
import com.scribner.util.UniversalImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment {

    private static final String TAG = "EditProfileFragment";

    private CircleImageView mProfilePhoto;

    /****** firebase stuff ******/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    /****** edit profile fragment widgets *****/
    private EditText mDisplayName, mUsername, mWebsite, mDescription, mEmail, mPhoneNumber;
    private TextView mChangeProfilePhoto;

    private UserSettings mUserSettings;

    private String userId;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(com.scribner.instagram.R.layout.fragment_editprofile, container, false);
        mProfilePhoto = view.findViewById(com.scribner.instagram.R.id.profile_photo);
        mDisplayName = view.findViewById(R.id.display_name);
        mUsername = view.findViewById(R.id.etUsername);
        mWebsite = view.findViewById(R.id.website);
        mDescription = view.findViewById(R.id.description);
        mEmail = view.findViewById(R.id.email);
        mPhoneNumber = view.findViewById(R.id.phone_number);
        mChangeProfilePhoto = view.findViewById(R.id.changeProfilePhoto);
        mFirebaseMethods = new FirebaseMethods(getActivity());

//        setProfileImage();
            setUpFireBaseAuth();

        //back button
        ImageView backArrow = view.findViewById(com.scribner.instagram.R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to profile");
                getActivity().finish();
            }
        });
        
        ImageView checkMark = view.findViewById(R.id.saveChanges);
        checkMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: saving new settings");
                saveProfileSettings();
            }
        });

        return view;
    }

    /**
     * Retrieves data from widgets and submits it to the database
     * Before execution it checks for a unique username
     */
    private void saveProfileSettings(){
        final String displayName = mDisplayName.getText().toString();
        final String username = mUsername.getText().toString();
        final String website = mWebsite.getText().toString();
        final String description = mDescription.getText().toString();
        final String email = mEmail.getText().toString();
        final long phoneNumber = Long.parseLong(mPhoneNumber.getText().toString());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                //user did not change their username
                if(!mUserSettings.getUser().getUsername().equals(username)){
                    usernameLookup(username);
                }
                //user totally changed their username. check for unique value
                else{

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * checks if username already exists in database
     * @param username
     */
    private void usernameLookup(final String username) {
        Log.d(TAG, "usernameLookup: " + username);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query q = ref
                //look for table
                .child(getString(R.string.dbname_users))
                //look for field in table
                .orderByChild(getString(R.string.field_username))
                .equalTo(username);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    //add username
                    mFirebaseMethods.updateUsername(username);
                    Toast.makeText(getActivity(), "Saved new settings", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }

                for(DataSnapshot singleDS : dataSnapshot.getChildren()){
                    if(singleDS.exists()){
                        Log.d(TAG, "usernameLookup: MATCH FOUND: " + singleDS.getValue(User.class).getUsername());
                        Toast.makeText(getActivity(), "Username already exists", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setProfileWidgets(UserSettings userSettings){
        //Log.d(TAG, "setProfileWidgets: setting widgets with data from firebase database\n" + userSettings.getSettings().toString());

        UserAccountSettings settings = userSettings.getSettings();

        //setup profile photo
        UniversalImageLoader.setImage(settings.getProfile_photo(), mProfilePhoto, null, "");

        mUserSettings = userSettings;
        //setup all the user text for individual profiles
        mDisplayName.setText(settings.getDisplay_name());
        mUsername.setText(settings.getUsername());
        mWebsite.setText(settings.getWebsite());
        mDescription.setText(settings.getDescription());
        mEmail.setText(userSettings.getUser().getEmail());
        mPhoneNumber.setText(String.valueOf(userSettings.getUser().getPhone_number()));
    }

//    private void setProfileImage(){
//        Log.d(TAG, "setProfileImage: setting profile image");
//        String imgURL = "http://getdrawings.com/img/generic-silhouette-10.jpg";
//
//        UniversalImageLoader.setImage(imgURL, mProfilePhoto, null,"");
//
//    }

    /*********************************************FIRE BASE***************************************/

    /**
     * set up firebase authentication object
     */
    private void setUpFireBaseAuth(){
        Log.d(TAG, "setUpFireBaseAuth: setting up firebase authorization");
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        userId = mAuth.getCurrentUser().getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
//                checkCurrentUser(user);
                if(user != null){
                    //signed in
                    Log.d(TAG, "onAuthStateChanged: signed_in: " + user.getUid());
                }else{
                    //signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }
            }
        };

        //the thing actually talking to firebase. gets and sets data
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //retrieve user info from firebase database
                setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));

                //retrieve image for user in question
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    private void checkCurrentUser(FirebaseUser user){
//        Log.d(TAG, "checkCurrentUser: checking if a user is logged in");
//        if(user == null){
//            Intent intent = new Intent(mContext, LoginActivity.class);
//            startActivity(intent);
//        }
//    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
//        checkCurrentUser(mAuth.getCurrentUser());

    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
