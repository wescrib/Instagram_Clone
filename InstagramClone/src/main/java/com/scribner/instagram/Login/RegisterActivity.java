package com.scribner.instagram.Login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.scribner.instagram.Models.User;
import com.scribner.instagram.R;
import com.scribner.util.FirebaseMethods;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebaseMethods;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private Context mContext;
    private ProgressBar mProgressBar;
    private EditText mEmail, mPassword, mUsername, mPhoneNumber;
    private TextView mLoadingPleaseWait;
    private Button btnRegister;

    private String email, password, username, phoneNumber;

    private String append = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d(TAG, "onCreate: started");
        mContext = RegisterActivity.this;
        firebaseMethods = new FirebaseMethods(mContext);
        initWidgets();
        setupFirebaseAuth();
        init();

    }

    private void init() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmail.getText().toString();
                password = mPassword.getText().toString();
                username = mUsername.getText().toString();
                phoneNumber = mPhoneNumber.getText().toString();

                if (isNotMissingInputs(email, username, password, phoneNumber)) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mLoadingPleaseWait.setVisibility(View.VISIBLE);

                    firebaseMethods.registerNewEmail(email, password, username, phoneNumber);
                }
            }
        });
    }

    private boolean isNotMissingInputs(String email, String username, String password, String phoneNumber) {
        if (email.equals("") || username.equals("") || password.equals("") || phoneNumber.equals("")) {
            Toast.makeText(mContext, "Missing a field", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * init activity widgets
     */
    private void initWidgets() {
        Log.d(TAG, "initWidgets: initializing widgets");
        mProgressBar = findViewById(R.id.progressBar);
        mLoadingPleaseWait = findViewById(R.id.loadingpleasewait);
        mEmail = findViewById(R.id.input_email);
        mPassword = findViewById(R.id.input_password);
        mUsername = findViewById(R.id.input_username);
        mPhoneNumber = findViewById(R.id.input_phonenumber);
        mContext = RegisterActivity.this;
        btnRegister = findViewById(R.id.btn_register);

        mProgressBar.setVisibility(View.GONE);
        mLoadingPleaseWait.setVisibility(View.GONE);
    }

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

                for (DataSnapshot singleDS : dataSnapshot.getChildren()) {
                    if (singleDS.exists()) {
                        Log.d(TAG, "usernameLookup: MATCH FOUND: " + singleDS.getValue(User.class).getUsername());
                        append = myRef.push().getKey().substring(3, 10);
                        Log.d(TAG, "onDataChange: username already exists. Appending random string to name: " + append);
                    }
                }

                //1st check: Make sure the username is not already in use
                String newUsername = "";
                newUsername = username + append;

                //add new user to the database
                firebaseMethods.addNewUser(email, newUsername, "", "", "");

                Toast.makeText(mContext, "Signup successful. Sending verification email.", Toast.LENGTH_SHORT).show();

                mAuth.signOut();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    /**
     * set up firebase authentication object
     */
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            usernameLookup(username);
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(TAG, "onCancelled: ", databaseError.toException());
                        }
                    });

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private boolean isStringNull(String string) {
        Log.d(TAG, "isStringNull: checking if string is null");
        if (string.equals("")) {
            return true;
        } else {
            return false;
        }
    }
}
