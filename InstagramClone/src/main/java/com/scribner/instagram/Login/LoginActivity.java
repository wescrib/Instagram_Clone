package com.scribner.instagram.Login;

import android.content.Context;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.scribner.instagram.Home.HomeActivity;
import com.scribner.instagram.R;

public class LoginActivity extends AppCompatActivity{

    private static final String TAG = "LoginActivity";

    /**
     * firebase
     */
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Context mContext;
    private ProgressBar mProgressBar;
    private EditText mEmail, mPassword;
    private TextView mPleaseWait;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgressBar = findViewById(R.id.progressBar);
        mPleaseWait = findViewById(R.id.pleaseWait);
        mEmail = findViewById(R.id.input_email);
        mPassword = findViewById(R.id.input_password);
        mContext = LoginActivity.this;
        Log.d(TAG, "onCreate: started");

        mProgressBar.setVisibility(View.GONE);
        mPleaseWait.setVisibility(View.GONE);

        setUpFireBaseAuth();
        init();
    }

    private boolean isStringNull(String string){
        Log.d(TAG, "isStringNull: checking if string is null");
        if(string.equals("")){
            return true;
        }else{
            return false;
        }
    }

    /*********************************************FIRE BASE***************************************/

    private void init(){
        //initialize login btn
        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attemping to login");

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                if(isStringNull(email) && isStringNull(password)){
                    Toast.makeText(mContext, "Email and password required", Toast.LENGTH_SHORT).show();
                }else{
                    mProgressBar.setVisibility(View.VISIBLE);
                    mPleaseWait.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    if (!task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                    Log.w(TAG, "signInWithEmail: FAILURE");
                                        mProgressBar.setVisibility(View.GONE);
                                        mPleaseWait.setVisibility(View.GONE);
                                        Toast.makeText(mContext, "Failed to Authenticate", Toast.LENGTH_SHORT).show();
                                        mProgressBar.setVisibility(View.GONE);
                                        mPleaseWait.setVisibility(View.GONE);

                                    } else {
                                        try{
                                            //if email is verified, login, else toast that it has not been verified.
                                            if(user.isEmailVerified()){
                                                Log.d(TAG, "onComplete: email is VERIFIED");
                                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }else{
                                                Toast.makeText(mContext, "Email is not verified", Toast.LENGTH_SHORT).show();
                                                mProgressBar.setVisibility(View.GONE);
                                                mPleaseWait.setVisibility(View.GONE);
                                                mAuth.signOut();
                                            }
                                        }catch (NullPointerException npe){
                                            Log.e(TAG, "onComplete: " + npe.getMessage());
                                        }

                                    }

                                    // ...
                                }
                            });


                }
            }
        });
        TextView linkSignUp = findViewById(R.id.link_signup);
        linkSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to register screen");
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        /**
         * if user is logged in, go to HomeActivity
         */
        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
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
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }
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
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
