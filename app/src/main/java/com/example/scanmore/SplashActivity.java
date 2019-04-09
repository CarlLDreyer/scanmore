package com.example.scanmore;

import android.content.Intent;
import android.os.Bundle;


import com.example.scanmore.Utils.PreferenceUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(PreferenceUtils.getUserLoggedInStatus(this) || !PreferenceUtils.getLoggedInEmailUser(this).equals("")){
            System.out.println("Pref is " + PreferenceUtils.getUserLoggedInStatus(this));
            Intent intentDos = new Intent(this, MainActivity.class);
            startActivity(intentDos);
        }
        else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            System.out.println("else");
        }
        //finish();
    }
}