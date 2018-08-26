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

        if(mAuth.getCurrentUser() != null){
            userId = mAuth.getCurrentUser().getUid();
        }
    }

    public boolean checkIfUsernameExists(String username, DataSnapshot snapshot){
        Log.d(TAG, "checkIfUsernameExists: checking if " + username + "exists");
        User user = new User();

        //looping through firebase datastore
        for(DataSnapshot ds: snapshot.child(userId).getChildren()){
            Log.d(TAG, "checkIfUsernameExists: " + ds);

            //attaching username to user object
            user.setUsername(ds.getValue(User.class).getUsername());

            //comparing username being applied for matches any username in firebase db
            if(StringManipulation.expandUsername(user.getUsername()).equals(username)){
                Log.d(TAG, "checkIfUsernameExists: FOUND MATCH: " + user.getUsername());
                return true;
            }
        }
        return false;
    }


    /**
     * Reigster a new email and password to Firebase Authentication
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

    public void sendVerificationEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(mContext, "Could not send verification email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    /**
     * Add info to the users nodes
     * Add info to the user_account_settings node
     * @param email
     * @param username
     * @param description
     * @param website
     * @param profile_photo
     */
    public void addNewUser(String email, String username, String description, String website, String profile_photo){
        User user = new User(userId, 1, email, StringManipulation.collapseUsername(username).toLowerCase());
        myRef.child(mContext.getString(R.string.dbname_users))
                //takes user id and builds a new document in firebase basically
                .child(userId)
                .setValue(user);

        UserAccountSettings settings = new UserAccountSettings(description,
                username,
                profile_photo,
                username,
                website,
                0,
                0,
                0
        );

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userId)
                .setValue(settings);
    }
}
