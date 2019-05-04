package com.example.scanmore;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.scanmore.Database.DatabaseHandler;
import com.example.scanmore.Database.User;
import com.example.scanmore.LoginActivity;
import com.example.scanmore.R;

import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ProfileActivity extends AppCompatActivity {

    TextView emailView;
    TextView nameView;
    DatabaseHandler db;

    private EditText number;
    private EditText address;

    LoginActivity la = LoginActivity.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupToolbar();
        db = new DatabaseHandler(this);
        final List<User> users = db.getAllUsers();

        User user = la.getActiveUser();

        nameView = findViewById(R.id.profile_name);
        nameView.setText(user.getName());
        //nameView.append(user.getName());
        emailView = findViewById(R.id.email_profile);
        emailView.setText(user.getEmail());

        number = findViewById(R.id.mobile_profile);
        number.setEnabled(false);

        address = findViewById(R.id.street_profile);
        address.setEnabled(false);

        ImageButton settings = (ImageButton) findViewById(R.id.settings_button);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                address.setEnabled(true);
                address.setEnabled(true);

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
