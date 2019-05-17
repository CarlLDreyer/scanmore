package com.example.scanmore;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scanmore.Database.DatabaseHandler;
import com.example.scanmore.Payment.CheckoutActivity;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.w3c.dom.Text;

public class SwishActivity extends AppCompatActivity {
    CheckoutActivity ca = CheckoutActivity.getInstance();
    EditText phoneNumber;
    private TextInputLayout phoneNumberWrapper;
    private static SwishActivity sInstance = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swish);
        sInstance = this;
        final DatabaseHandler databaseHandler = new DatabaseHandler(this);
        setupToolbar();
        phoneNumber = (EditText) findViewById(R.id.phone_number_input);
        phoneNumberWrapper = (TextInputLayout) findViewById(R.id.phone_number_input_wrapper);
        final Button submitButton = (Button) findViewById(R.id.submit_swish);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phoneNumber.getText().toString().length() >= 10){
                        databaseHandler.insertSwish(phoneNumber.getText().toString());
                        ca.addPaymentMethod(phoneNumber.getText().toString(), R.drawable.ic_swish);
                        ca.initTextViews();
                        Toast.makeText(SwishActivity.this, "Swish payment added successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                }
                else{
                    System.out.println("Invalid number");
                    phoneNumber.setError("Please enter a valid number!");
                    phoneNumberWrapper.setBackgroundResource(R.drawable.text_input_border_error);
                }


            }
        });
        phoneNumber.requestFocus();

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(phoneNumber.getText().toString().length() >= 10){
                    phoneNumberWrapper.setBackgroundResource(R.drawable.text_input_border);
                }
            }
        });
    }
    public static SwishActivity getInstance() {
        return sInstance;
    }

    public EditText getPhoneNumber(){
        return phoneNumber;
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
