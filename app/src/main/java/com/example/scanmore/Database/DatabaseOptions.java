package com.example.scanmore.Database;

public class DatabaseOptions {


    public static final String USERS_TABLE = "users";



    public static final String ID = "id";

    public static final String EMAIL = "email";

    public static final String PASSWORD = "password";



    public static final String CREATE_USERS_TABLE_ =
            "CREATE TABLE " + USERS_TABLE + "(" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    EMAIL + " TEXT NOT NULL," +
                    PASSWORD + " TEXT" +
                    ")";
}
