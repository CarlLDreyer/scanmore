package com.example.scanmore;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scanmore.Database.DatabaseHandler;
import com.example.scanmore.Database.User;
import com.example.scanmore.Utils.PreferenceUtils;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LoginActivity extends AppCompatActivity {

    private Button btSignIn;

    private Button btSignUp;

    private EditText edtEmail;

    private TextInputLayout edtPasswordWrapper;

    private TextInputLayout edtEmailWrapper;

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

        edtEmailWrapper = findViewById(R.id.edt_email_wrapper);

        edtPasswordWrapper = findViewById(R.id.edt_password_wrapper);
        edtPasswordWrapper.setPasswordVisibilityToggleEnabled(true);

        edtPassword = findViewById(R.id.passwordinput);


        final DatabaseHandler dbHelper = new DatabaseHandler(this);

        //this button should open the signup activity

        btSignUp.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivityForResult(i, 1);

            }

        });

//LOGIN button method
        btSignIn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                if(!isEmailFormEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()){
                    user = dbHelper.getUser(edtEmail.getText().toString());
                    if(user != null){
                        if(!isPasswordFormEmpty()){
                            if(user.getPassword().equals(edtPassword.getText().toString())){
                                Bundle mBundle = new Bundle();

                                mBundle.putString("user", user.getEmail());
                                verifySQL(user);
                                setActiveUser(user);

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtras(mBundle);
                                startActivity(intent);
                            }
                            else{
                                edtPasswordWrapper.setError("Incorrect password");
                            }
                        }
                        else{
                            edtPasswordWrapper.setError("Please enter a valid password");
                        }
                    }
                    else{
                        edtEmailWrapper.setError("User not found");
                    }
                }
                else{
                    edtEmailWrapper.setError("Please enter a valid email");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String result=data.getStringExtra("result");
                edtEmail.setText(result);
                System.out.println(result);
            }
        }
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
        if(sInstance == null){
            sInstance = new LoginActivity();
        }
        return sInstance ;
    }

    private boolean isEmailFormEmpty(){
        return TextUtils.isEmpty(edtEmail.getText().toString());
    }

    private boolean isPasswordFormEmpty(){
        return TextUtils.isEmpty(edtPassword.getText().toString());
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


