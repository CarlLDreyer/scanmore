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
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivityForResult(i, 1);

            }

        });

//LOGIN button method
        btSignIn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                if (!emptyValidation()) {

                    user = dbHelper.getUser(edtEmail.getText().toString());

                    if (user != null && user.getPassword().equals(edtPassword.getText().toString())) {

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
                    }
                    else if(user == null){
                        edtEmail.setError("User not found");
                        edtPassword.setText("");

                    }

                    else if(!(user.getPassword().equals(edtPassword.getText().toString()))){
                        edtPassword.setError("Incorrect password");
                        edtPassword.setText("");
                    }
                    else if(user != null && (user.getPassword().equals(""))){
                        edtPassword.setError("Incorrect password");
                        edtPassword.setText("");
                    }

                }else{
                    edtPassword.setError(null);
                    Toast.makeText(LoginActivity.this, "Empty Fields", Toast.LENGTH_SHORT).show();

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


