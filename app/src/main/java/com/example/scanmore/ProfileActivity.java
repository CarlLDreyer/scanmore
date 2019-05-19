package com.example.scanmore;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    private Button buttonDelete;

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
        buttonDelete = (Button)findViewById(R.id.btn_deleteProfile);
        address = (TextView)findViewById(R.id.street_profile);
        address.setText("");
        address.setText(address1);
        phone = (TextView)findViewById(R.id.mobile_profile);
        phone.setText("");
        phone.setText(phone1);
        db = new DatabaseHandler(this);
        final List<User> users = db.getAllUsers();

        User user = la.getActiveUser();
        nameView = findViewById(R.id.profile_name);
        nameView.setText(user.getName());
        emailView = findViewById(R.id.email_profile);
        emailView.setText(user.getEmail());
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

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProfileAlert();
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

    public void deleteProfileAlert(){
        final User activeUser2 = la.getActiveUser();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Remove profile");
        alert.setMessage("Are you sure you want to remove this proofile?");
        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              db.deleteUser(activeUser2.getEmail());

              Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
              startActivity(intent);

            }
        });
        alert.setNegativeButton(R.string.avbryt, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

}