package com.example.scanmore;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scanmore.Database.DatabaseHandler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SwishActivity extends AppCompatActivity {
    PayActivity pa = PayActivity.getInstance();
    EditText phoneNumber;
    private static SwishActivity sInstance = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swish);
        sInstance = this;
        setupToolbar();
        final DatabaseHandler databaseHandler = new DatabaseHandler(this);
        phoneNumber = (EditText) findViewById(R.id.phone_number_input);
        Button submitButton = (Button) findViewById(R.id.submit_swish);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHandler.insertSwish(phoneNumber.getText().toString());
                pa.addFragmentSwish(phoneNumber);
                Toast.makeText(SwishActivity.this, "Swish payment added successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        phoneNumber.requestFocus();
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
