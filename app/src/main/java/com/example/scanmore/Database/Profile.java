package com.example.scanmore.Database;

public class Profile {

    public static final String TABLE_NAME = "profiles";

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_PROFILE_NAME = "name";
    public static final String COLUMN_PROFILE_EMAIL = "email";
    public static final String COLUMN_PROFILE_NUMBER = "number";

    private int id;
    private String name;
    private String email;
    private int number;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PROFILE_NAME + " TEXT,"
                    + COLUMN_PROFILE_EMAIL + " TEXT,"
                    + COLUMN_PROFILE_NUMBER + " INTEGER"
                    + ")";

    public Profile() {
    }

    public Profile(int id, String name, String email, int number) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.number = number;


    }




}