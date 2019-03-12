package com.example.scanmore.Database;


import android.text.Editable;

public class Swish {
    public static final String TABLE_NAME = "swish";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PHONE = "phone";


    private int id;
    private String phone;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PHONE + " INT"
                    + ")";

    public Swish() {
    }

    public Swish(int id, String phone) {
        this.id = id;
        this.phone = phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phone;
    }

    public void setPhoneNumberString(String phone) {
        this.phone = phone;
    }

}
