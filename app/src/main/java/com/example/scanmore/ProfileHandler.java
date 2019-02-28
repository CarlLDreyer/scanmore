package com.example.scanmore;

import android.provider.ContactsContract;

import java.util.HashMap;
import java.util.Observable;

public class ProfileHandler extends Observable {

    private static final  ProfileHandler ourInstance = new ProfileHandler();
    private HashMap<String, Integer> iconArray;
    private HashMap<Integer, Profile> profileArray;
    private Profile activeProfile;

    private ProfileHandler(){

    }
    public static ProfileHandler getInstance(){return ourInstance;}

    private  void getProfileArray(){
        if(profileArray == null){
            profileArray = new HashMap<Integer, Profile>();
            profileArray.put(1, activeProfile);
            profileArray.put(2, new Profile("Jenny"));
        }
    }

}
