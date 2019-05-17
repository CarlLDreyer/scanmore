package com.example.scanmore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

public class Settingsactivity extends AppCompatActivity {

    private static Settingsactivity settingInstance = null;
    SharedPreferences sharedPreferences;

    //this button will "save the changes"
    Button confirm;

    EditText edtPhoneNumber;
    EditText edtAddress;

    TextInputLayout edtPhoneWrapper;
    TextInputLayout edtAddressWrapper;

    private String addressR;
    private String phoneR;
    /*
TextView address = findViewById(R.id.street_p);
TextView phone = findViewById(R.id.mobile_p);
      */
    public static final String mypreference = "mypref";
    public static final String PHONE = "phoneKey";
    public static final String ADDRESS = "addressKey";

    private String text1;
    private String text2;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingsactivity);
        setupToolbar();
        confirm = (Button)findViewById(R.id.btn_setSettings);
        edtPhoneNumber = (EditText) findViewById(R.id.input_phone);
        edtAddress = (EditText)findViewById(R.id.input_address);

        sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(ADDRESS)){
            edtAddress.setText(sharedPreferences.getString(ADDRESS,""));
        }
        if(sharedPreferences.contains(PHONE)){
            edtPhoneNumber.setText(sharedPreferences.getString(PHONE, ""));
        }
        edtAddressWrapper = (TextInputLayout)findViewById(R.id.input_address_label);
        edtPhoneWrapper = (TextInputLayout)findViewById(R.id.input_phone_label);

        edtAddress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isAddressValid();
                return false;
            }
        });

        edtPhoneNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isPhoneNumberValid();
                return false;
            }
        });


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate2()) {
                    onConfirmedFail();
                    return;
                }
                else {
                    confirm();

                    Intent returnIntent = new Intent(Settingsactivity.this,ProfileActivity.class);
                    /*
                    String phone = edtPhoneNumber.getText().toString();
                    String address = edtAddress.getText().toString();
*/
                    returnIntent.putExtra("phone", getPhoneNumber() );
                    returnIntent.putExtra("address", getAddress());
                    startActivityForResult(returnIntent, 1);

                }
            }
        });


        settingInstance =  this;

    }

    //checks if the inputs are valid
    public boolean validate2() {
        boolean valid2 = true;
        String phone = edtPhoneNumber.getText().toString();
        String address = edtAddress.getText().toString();

        if (phone.length()<10 || !Patterns.PHONE.matcher(phone).matches()) {
            edtPhoneWrapper.setError("Phone must be atleast 10 didgits");
            valid2 = false;
        } else {
            edtPhoneNumber.setError(null);
        }

        if(address.length()<1){
            edtAddressWrapper.setError("Address cannot be 0");
            valid2 = false;
        }else{
            edtAddress.setError(null);
        }
        return valid2;
    }

    public void onConfirmedFail() {
        Toast.makeText(getBaseContext(), "Confirmation failed", Toast.LENGTH_LONG).show();
        confirm.setEnabled(true);
    }

    public void confirm() {
        confirm.setEnabled(false);
        Toast.makeText(Settingsactivity.this, "Changes confirmed", Toast.LENGTH_SHORT).show();
        String a = edtAddress.getText().toString();
        String p = edtPhoneNumber.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ADDRESS, a);
        editor.putString(PHONE, p);
        editor.commit();
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
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void isPhoneNumberValid(){
        String phone = edtPhoneNumber.getText().toString();
        boolean validPhoneNumber = phone.length()<1 || !Patterns.PHONE.matcher(phone).matches();
        if(validPhoneNumber){
            edtPhoneWrapper.setError("Please enter a valid phone number");
        }

        else{
            edtPhoneWrapper.setError(null);
        }
    }

    public void isAddressValid(){
        String address = edtAddress.getText().toString();
        boolean validAddress = address.isEmpty() || address.length() < 1;
        if(validAddress){
            edtAddressWrapper.setError("Please enter a valid address");
        }
        else{
            edtAddressWrapper.setError(null);
        }
    }

    public String getAddress(){

        edtAddress = (EditText) findViewById(R.id.input_address);
        sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(ADDRESS)){
            addressR = sharedPreferences.getString(ADDRESS, "");
        }
        return addressR;
    }

    public String getPhoneNumber(){
        edtPhoneNumber = (EditText)findViewById(R.id.input_phone);
        sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(PHONE)){
            phoneR = sharedPreferences.getString(PHONE, "");
        }
        return phoneR;
    }

    public static Settingsactivity getInstanceSetting() {
        if(settingInstance == null){
            settingInstance = new Settingsactivity();
        }
        return settingInstance ;
    }


}