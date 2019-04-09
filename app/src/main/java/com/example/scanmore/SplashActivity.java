package com.example.scanmore;

import android.content.Intent;
import android.os.Bundle;

import com.example.scanmore.Database.DatabaseHandler;
import com.example.scanmore.Database.User;
import com.example.scanmore.Utils.PreferenceUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private LoginActivity la;
    private DatabaseHandler dh = new DatabaseHandler(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        la = LoginActivity.getInstance();
        if(PreferenceUtils.getUserLoggedInStatus(this) || !PreferenceUtils.getLoggedInEmailUser(this).equals("")){
            User activeUser = dh.getUser(PreferenceUtils.getLoggedInEmailUser(this));
            la.setActiveUser(activeUser);
            Intent intentDos = new Intent(this, MainActivity.class);
            startActivity(intentDos);
        }
        else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}