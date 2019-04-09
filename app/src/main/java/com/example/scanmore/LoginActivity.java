package com.example.scanmore;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scanmore.Database.DatabaseHandler;
import com.example.scanmore.Database.User;
import com.example.scanmore.Utils.PreferenceUtils;
import com.google.android.material.navigation.NavigationView;

import SignUp.SignupActivity;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LoginActivity extends AppCompatActivity {

    private Button btSignIn;

    private Button btSignUp;

    private EditText edtEmail;

    private EditText edtPassword;

    private User user;

    private static LoginActivity sInstance = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sInstance = this;

        setupToolbar();

        btSignIn = findViewById(R.id.btSignIn);

        btSignUp = findViewById(R.id.btSignUp);

        edtEmail = findViewById(R.id.emailinput);

        edtPassword = findViewById(R.id.passwordinput);

        final DatabaseHandler dbHelper = new DatabaseHandler(this);

        //this button should open the signup activity

        btSignUp.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);

                startActivity(intent);

            }

        });

//LOGIN button method
        btSignIn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                if (!emptyValidation()) {

                    // user = dbHelper.queryUser(edtEmail.getText().toString(), edtPassword.getText().toString());
                    user = dbHelper.getUser(edtEmail.getText().toString(), edtPassword.getText().toString());

                    if (user != null) {

                        Bundle mBundle = new Bundle();

                        mBundle.putString("user", user.getEmail());
                        verifySQL(user);
                        setActiveUser(user);

                        //will log in and open main activity

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                        intent.putExtras(mBundle);

                        startActivity(intent);
//welcome message
                        Toast.makeText(LoginActivity.this, "Welcome " + user.getName(), Toast.LENGTH_SHORT).show();

                        //user not found
                    } else {

                        Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();

                        edtPassword.setText("");

                    }
//if LOGIN button is clicked and the fields are empty
                }else{

                    Toast.makeText(LoginActivity.this, "Empty Fields", Toast.LENGTH_SHORT).show();

                }

            }

        });
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static LoginActivity getInstance() {
        return sInstance ;
    }


    private boolean emptyValidation() {

        if (TextUtils.isEmpty(edtEmail.getText().toString()) || TextUtils.isEmpty(edtPassword.getText().toString())) {
            return true;
        }else {
            return false;
        }
    }

    private void verifySQL(User user){
        PreferenceUtils.setLoggedInUserEmail(this, user.getEmail());
        PreferenceUtils.setUserLoggedInStatus(this, true);
    }

    public void setActiveUser(User user){
        this.user = user;
    }
    public User getActiveUser(){
        return user;
    }


    }


