package com.example.scanmore;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.scanmore.Database.DatabaseHandler;
import com.example.scanmore.Payment.CheckoutActivity;
import com.example.scanmore.Scanner.ScanActivity;
import com.example.scanmore.Utils.DataHolder;
import com.example.scanmore.Utils.PreferenceUtils;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;
    private DataHolder dataholder;

    TextView textView;
    ImageView iv;
    LoginActivity la = LoginActivity.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataholder = DataHolder.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupMainContent();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navName = (TextView) headerView.findViewById(R.id.nav_name);
        CircleImageView profilePicture = (CircleImageView) headerView.findViewById(R.id.profile_picture);
        TextView navEmail = (TextView) headerView.findViewById(R.id.nav_email);
        try{
            navName.setText(la.getActiveUser().getName());
            navEmail.setText(la.getActiveUser().getEmail());
            profilePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchActivity(ProfileActivity.class);
                }
            });

        }
        catch(NullPointerException e){ e.printStackTrace();}

        DatabaseHandler databaseHandler = new DatabaseHandler(this);

        setupDatabaseInserts(databaseHandler);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_profile) {
            // Handle the camera action
            launchActivity(ProfileActivity.class);
        }
        else if (id == R.id.nav_scanner) {
            launchActivity(ScanActivity.class);
        }
        else if (id == R.id.nav_checkout) {
            launchActivity(CheckoutActivity.class);
        }
        else if (id == R.id.nav_shoppinglist) {
            launchActivity(ToDoList.class);
        }
        else if (id == R.id.nav_logout) {
            PreferenceUtils.setLoggedInUserEmail(this, "");
            PreferenceUtils.setUserLoggedInStatus(this, false);
            dataholder.products.clear();
            launchActivity(LoginActivity.class);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setupMainContent(){
        Calendar c = Calendar.getInstance();
        String[] monthName = {"January", "February",
                "March", "April", "May", "June", "July",
                "August", "September", "October", "November",
                "December"};
        String month = monthName[c.get(Calendar.MONTH)];
        TextView welcomeName = (TextView) findViewById(R.id.welcome_name);
        welcomeName.setText(currentHour() + trimFirstWord(la.getActiveUser().getName()) + "!");
        TextView mainMonth = (TextView) findViewById(R.id.main_month);
        mainMonth.setText(month);

        Button scanMain = (Button) findViewById(R.id.scan_button_main);
        scanMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(ScanActivity.class);
            }
        });
        Button cartMain = (Button) findViewById(R.id.cart_button_main);
        cartMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(ToDoList.class);
            }
        });
        Button payMain = (Button) findViewById(R.id.pay_button_main);
        payMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(CheckoutActivity.class);
            }
        });

    }

    public String trimFirstWord(String s) {
        String[] sp = s.split(" ");
        return sp[0];
    }

    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivity(intent);
        }
    }

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    public String currentHour(){
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR);

        if(1 <= hours && hours <= 5){
            return "Good Midnight ";
        }
        else if(6 <= hours && hours <= 10){
            return "Good Morning ";
        }else if(10 <= hours && hours <= 12){
            return "Good Afternoon ";
        }else if(13 <= hours && hours <= 17){
            return "Good Aftermidday ";
        }else if(18 <= hours && hours <= 22){
            return "Good Evening! ";
        }else {
            return "Good Night ";
        }

    }

    public void setupDatabaseInserts(DatabaseHandler db){
        db.insertProduct("7350015508279", "Lundgrens ", 29);
        db.insertProduct("7332945033038", "Conmore Vatten ", 80);
        db.insertProduct("7610313412143", "Örtsalt ", 23);
        db.insertProduct("7311310040598", "Peppar ", 16);
        db.insertProduct("8715800002315", "Salt ", 13);
        db.insertProduct("7350002400340", "Olivolja ", 40);
        db.insertProduct("7350002400531", "Vinäger ", 30);
        db.insertProduct("7311310027117", "Citron Peppar", 13);
        db.insertProduct("7332945033038", "Vatten ", 56);
        db.insertProduct("7391835917292", "Rooibos Original ", 21);
        db.insertProduct("7311250004797", "Rapé Sparkling", 32);
    }
}
