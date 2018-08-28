package com.scribner.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.scribner.instagram.Models.User;
import com.scribner.instagram.Models.UserAccountSettings;
import com.scribner.instagram.Models.UserSettings;
import com.scribner.instagram.R;

public class FirebaseMethods {
    private static final String TAG = "FirebaseMethods";
    private String userId;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private Context mContext;

    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mContext = context;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        if (mAuth.getCurrentUser() != null) {
            userId = mAuth.getCurrentUser().getUid();
        }
    }

    public boolean checkIfUsernameExists(String username, DataSnapshot snapshot) {
        Log.d(TAG, "checkIfUsernameExists: checking if " + username + "exists");
        User user = new User();

        //looping through firebase datastore
        for (DataSnapshot ds : snapshot.child(userId).getChildren()) {
            Log.d(TAG, "checkIfUsernameExists: " + ds);

            //attaching username to user object
            user.setUsername(ds.getValue(User.class).getUsername());

            //comparing username being applied for matches any username in firebase db
            if (StringManipulation.expandUsername(user.getUsername()).equals(username)) {
                Log.d(TAG, "checkIfUsernameExists: FOUND MATCH: " + user.getUsername());
                return true;
            }
        }
        return false;
    }


    /**
     * Reigster a new email and password to Firebase Authentication
     *
     * @param email
     * @param password
     * @param username
     * @param telephoneNumber
     */
    public void registerNewEmail(final String email, String password, final String username, final String telephoneNumber) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            userId = mAuth.getCurrentUser().getUid();
                            sendVerificationEmail();
                            Log.d(TAG, "createUserWithEmail:success " + userId);


                            Toast.makeText(mContext, R.string.auth_success, Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(mContext, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(mContext, "Could not send verification email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    /**
     * Add info to the users nodes
     * Add info to the user_account_settings node
     *
     * @param email
     * @param username
     * @param description
     * @param website
     * @param profile_photo
     */
    public void addNewUser(String email, String username, String description, String website, String profile_photo) {
        User user = new User(userId, 1, email, StringManipulation.collapseUsername(username).toLowerCase());
        myRef.child(mContext.getString(R.string.dbname_users))
                //takes user id and builds a new document in firebase basically
                .child(userId)
                .setValue(user);

        UserAccountSettings settings = new UserAccountSettings(
                description,
                username,
                profile_photo,
                StringManipulation.collapseUsername(username).toLowerCase(),
                website,
                0,
                0,
                0
        );

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userId)
                .setValue(settings);
    }

    /**
     * Gets the account settings for the current user
     * Database: user_account_settings node
     *
     * @param dataSnap
     * @return
     */
    private UserSettings getUserSettings(DataSnapshot dataSnap) {
        Log.d(TAG, "getUserAccountSettings: getting user account settings from firebase");

        UserAccountSettings settings = new UserAccountSettings();
        User user = new User();

        //user_account_settings node
        for (DataSnapshot ds : dataSnap.getChildren()) {
            //if the key (the key is basically the name of the table) == user_account_settings.
            //its iterating through the database looking for the key?
            if (ds.getKey().equals(mContext.getString(R.string.dbname_user_account_settings))) {
                Log.d(TAG, "getUserAccountSettings: datasnapshot: " + ds);

                try {
                    settings.setDisplay_name(
                            ds.child(userId)
                                    .getValue(UserAccountSettings.class)
                                    .getDisplay_name()

                    );
                    settings.setDisplay_name(
                            ds.child(userId)
                                    .getValue(UserAccountSettings.class)
                                    .getUsername()

                    );
                    settings.setWebsite(
                            ds.child(userId)
                                    .getValue(UserAccountSettings.class)
                                    .getWebsite()

                    );

                    settings.setDescription(
                            ds.child(userId)
                                    .getValue(UserAccountSettings.class)
                                    .getDescription()

                    );
                    settings.setProfile_photo(
                            ds.child(userId)
                                    .getValue(UserAccountSettings.class)
                                    .getProfile_photo()

                    );
                    settings.setPosts(
                            ds.child(userId)
                                    .getValue(UserAccountSettings.class)
                                    .getPosts()

                    );
                    settings.setFollowers(
                            ds.child(userId)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowers()

                    );
                    settings.setFollowing(
                            ds.child(userId)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowing()

                    );

                    Log.d(TAG, "getUserAccountSettings: retreived user_account_settings: " + settings.toString());
                } catch (Exception e) {
                    Log.e(TAG, "getUserAccountSettings: " + e.getMessage());
                }
            }
            if (ds.getKey().equals(mContext.getString(R.string.dbname_users))) {
                Log.d(TAG, "getUserAccountSettings: datasnapshot: " + ds);

                user.setUsername(ds.child(userId)
                        .getValue(User.class)
                        .getUsername());

                user.setEmail(ds.child(userId)
                        .getValue(User.class)
                        .getEmail());

                user.setUser_id(ds.child(userId)
                        .getValue(User.class)
                        .getUser_id());

                user.setPhone_number(ds.child(userId)
                        .getValue(User.class)
                        .getPhone_number());

                Log.d(TAG, "getUserAccountSettings: retreived user: " + user.toString());
            }
        }

        return new UserSettings(user, settings);

    }
}
