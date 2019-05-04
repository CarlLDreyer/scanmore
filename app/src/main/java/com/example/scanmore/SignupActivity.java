package com.example.scanmore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scanmore.Database.DatabaseHandler;
import com.example.scanmore.Database.User;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    EditText edtName;
    EditText edtEmailSignup;
    EditText edtPasswordSignup;
    TextInputLayout edtNameWrapper;
    TextInputLayout edtEmailWrapper;
    TextInputLayout edtPasswordWrapper;
    TextView login;
    Button btnSignup;
    DatabaseHandler databaseHandler;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        edtName = findViewById(R.id.input_name);
        edtEmailSignup = findViewById(R.id.input_email);
        edtPasswordSignup = findViewById(R.id.input_password);
        edtNameWrapper = findViewById(R.id.input_name_wrapper);
        edtEmailWrapper = findViewById(R.id.input_email_wrapper);
        edtPasswordWrapper = findViewById(R.id.input_password_wrapper);
        edtPasswordWrapper.setPasswordVisibilityToggleEnabled(true);


        edtEmailSignup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isNameValid();
                return false;
            }
        });

        edtPasswordSignup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isEmailValid();
                return false;
            }
        });

        login = findViewById(R.id.link_login);
        btnSignup = findViewById(R.id.btn_signup);
        databaseHandler = new DatabaseHandler(this);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (!validate()) {
                onSignupFailed();
                return;
            }
            else {
                signup();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", edtEmailSignup.getText().toString());
                setResult(RESULT_OK, returnIntent);
                finish();
            }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void signup() {
        Log.d(TAG, "Signup");
        btnSignup.setEnabled(false);
        String name = edtName.getText().toString();
        String email = edtEmailSignup.getText().toString();
        String password = edtPasswordSignup.getText().toString();
        databaseHandler.addUser2(new User(name, email, password));
        Toast.makeText(SignupActivity.this, "Added User", Toast.LENGTH_SHORT).show();

    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        btnSignup.setEnabled(true);
    }

      @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

//checks if the inputs are valid
    public boolean validate() {
        boolean valid = true;
        String name = edtName.getText().toString();
        String email = edtEmailSignup.getText().toString();
        String password = edtPasswordSignup.getText().toString();
        User user = databaseHandler.getUser(email);
        if (name.isEmpty() || name.length() < 3) {
            edtNameWrapper.setError("Name must be atleast 3 characters");
            valid = false;
        } else {
            edtNameWrapper.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmailWrapper.setError("Please enter a valid email");
            valid = false;
        }
        else if(user != null){
            edtEmailWrapper.setError("Email already in use");
        }
        else {
            edtEmailWrapper.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            edtPasswordWrapper.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            edtPasswordWrapper.setError(null);
        }
        return valid;
    }


    public void isNameValid(){
        String name = edtName.getText().toString();
        boolean validName = name.isEmpty() || name.length() < 3;
        if(validName){
            edtNameWrapper.setError("Name must be atleast 3 characters");
        }
        else{
            edtNameWrapper.setError(null);
        }
    }

    public void isEmailValid(){
        String email = edtEmailSignup.getText().toString();
        boolean validEmail = email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if(validEmail){
            edtEmailWrapper.setError("Please enter a valid email");
        }
        else{
            User user = databaseHandler.getUser(email);
            if(user != null){
                edtEmailWrapper.setError("Email already in use");
            }
            else{
                edtEmailWrapper.setError(null);
            }
        }
    }

    public void isPasswordValid(){
        String password = edtPasswordSignup.getText().toString();
        boolean validPassword = password.isEmpty() || password.length() < 4 || password.length() > 10;
        if(validPassword){
            edtPasswordWrapper.setError("Between 4 and 10 alphanumeric characters");
        }
        else{
            edtPasswordWrapper.setError(null);
        }
    }
}
