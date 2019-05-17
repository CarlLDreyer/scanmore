package com.example.scanmore;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.scanmore.Database.DatabaseHandler;
import com.example.scanmore.Database.User;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private TextView address;
    private TextView phone;

    TextView nameView;
    TextView emailView;
    DatabaseHandler db;

    LoginActivity la = LoginActivity.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();

        String address1 = intent.getStringExtra("address");
        String phone1 = intent.getStringExtra("phone");

        setupToolbar();
        address = (TextView)findViewById(R.id.street_profile);
        address.setText("");
        address.setText("Address: " + address1);
        phone = (TextView)findViewById(R.id.mobile_profile);
        phone.setText("");
        phone.setText("Phone number: " + phone1);
        db = new DatabaseHandler(this);
        final List<User> users = db.getAllUsers();

        User user = la.getActiveUser();
        nameView = findViewById(R.id.profile_name);
        nameView.setText(user.getName());
        emailView = findViewById(R.id.email_profile);
        emailView.setText("Email: " + user.getEmail());
        //nameView.append(user.getName());

        //Will open the settings activity
        ImageButton settings = (ImageButton) findViewById(R.id.btn_setSettings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, Settingsactivity.class);
                startActivity(i);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}