package com.example.scanmore;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.scanmore.Database.DatabaseHandler;
import com.example.scanmore.ShoppingList.ShoppingListActivity;
import com.google.android.material.navigation.NavigationView;

import Profile.ProfileActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        DatabaseHandler databaseHandler = new DatabaseHandler(this);

        /*
        final List<User> users = databaseHandler.getAllUsers();
        final User user = users.get(0);
        textView = findViewById(R.id.textView);

        textView.setText("Email: ");
        textView.append(user.getEmail());
*/
        setupDatabaseInserts(databaseHandler);



       // setupToolbar();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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


        if (id == R.id.nav_camera) {
            // Handle the camera action
            launchActivity(ScanActivity.class);

        }  else if (id == R.id.nav_share) {

        }
        else if (id == R.id.nav_pay) {
            launchActivity(PayActivity.class);
        }
        else if (id == R.id.nav_shoppinglist) {
launchActivity(ShoppingListActivity.class);

        }else if (id == R.id.nav_profile) {

          launchActivity(ProfileActivity.class);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    public void setupDatabaseInserts(DatabaseHandler db){

        /*
        db.insertProduct("7350015508279", "Lundgrens ", 29);
        db.insertProduct("7332945033038", "Conmore Vatten ", 80);
        db.insertProduct("7610313412143", "Örtsalt", 23);
        db.insertProduct("7311310040598", "Peppar", 16);
        db.insertProduct("8715800002315", "Salt", 13);
        db.insertProduct("7350002400340", "Olivolja", 40);
        db.insertProduct("7350002400531", "Vinäger", 30);
        db.insertProduct("7311310027117", "Citron Peppar", 13);
        db.insertProduct("7332945033038", "Vatten", 56);

        */

    }

}
