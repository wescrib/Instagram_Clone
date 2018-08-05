/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    EditText emailEditText;
    EditText passwordEditText;
    EditText usernameEditText;
    TextView signInTextview;

    Button signUpBtn;

    Boolean signUpModeActive = true;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    signInTextview = findViewById(R.id.signInBtn);
    signInTextview.setOnClickListener(this);
    passwordEditText = findViewById(R.id.passwordForm);
    usernameEditText = findViewById(R.id.usernameEditText);
    emailEditText = findViewById(R.id.emailForm);
    ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
    ImageView instagramLogo = findViewById(R.id.logo);


    passwordEditText.setOnKeyListener(this);
    constraintLayout.setOnClickListener(this);
    instagramLogo.setOnClickListener(this);

    if(ParseUser.getCurrentUser() != null){
        showAllUsers();
    }
    
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

  public void signUp(View view) {
      ParseUser user = new ParseUser();


      passwordEditText = findViewById(R.id.passwordForm);


      String errMsg = "";



      if (emailEditText.getText().toString().matches("") && passwordEditText.getText().toString().matches("") && usernameEditText.getText().toString().matches("")) {
          errMsg += "Username, Email, Password is required";
          Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
      }else if(usernameEditText.getText().toString().matches("")) {
          errMsg += "Username is required";
          Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
      } else if (emailEditText.getText().toString().matches("") && signUpModeActive) {
          errMsg += "Email is required";
          Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
      } else if(passwordEditText.getText().toString().matches("")){
              errMsg += "Password is required";
              Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
      }else{
          if(signUpModeActive) {
              user.setUsername(usernameEditText.getText().toString());
              user.setEmail(emailEditText.getText().toString());
              user.setPassword(passwordEditText.getText().toString());

              user.signUpInBackground(new SignUpCallback() {
                  @Override
                  public void done(ParseException e) {
                      if (e == null) {
                          showAllUsers();
                          Toast.makeText(MainActivity.this, "Registered!", Toast.LENGTH_SHORT).show();
                      } else {
                          Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                      }
                  }
              });
          }else{
              ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                  @Override
                  public void done(ParseUser user, ParseException e) {
                      if(user != null){
                          showAllUsers();
                      }else{
                          Toast.makeText(MainActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                      }
                  }
              });
          }
      }
  }

        @Override
        public void onClick(View v) {
        signUpBtn = findViewById(R.id.signUp);
        emailEditText = findViewById(R.id.emailForm);
        passwordEditText = findViewById(R.id.passwordForm);

        if(v.getId() == R.id.signInBtn){
            if(signUpModeActive){
                Log.i("Switch", "moving to login view");
                signUpModeActive = false;
                signUpBtn.setText("Sign In");
                signInTextview.setText("Sign Up");
                emailEditText.animate().alpha(0).setDuration(500);
                passwordEditText.animate().translationYBy(-255).setDuration(500);
                signUpBtn.animate().translationYBy(-255).setDuration(500);
                signInTextview.animate().translationYBy(-255).setDuration(500);

            }else{
                Log.i("Switch", "moving to register view");
                signUpModeActive = true;
                emailEditText.animate().alpha(1).setDuration(1000);
                signUpBtn.setText("Sign Up");
                signInTextview.setText("Sign In");
                passwordEditText.animate().translationYBy(255).setDuration(500);
                signUpBtn.animate().translationYBy(255).setDuration(500);
                signInTextview.animate().translationYBy(255).setDuration(500);
            }
            //get rid of the keyboard by clicking anywhere on the screen
        }else if (v.getId() == R.id.logo || v.getId() == R.id.constraintLayout){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

      if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == event.ACTION_DOWN){
          signUp(v);
      }
        return false;
    }

    public void showAllUsers(){
      Intent intent = new Intent(getApplication(), HomeActivity.class);

      startActivity(intent);
    }
}