package com.example.scanmore.Database;

import java.util.ArrayList;

public class Product {
    public static final String TABLE_NAME = "products";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EAN = "ean";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";


    private int id;
    private String ean;
    private String name;
    private int price;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_EAN + " TEXT UNIQUE,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_PRICE + " INTEGER"
                    + ")";

    public Product() {
    }

    public Product(int id, String ean, String name, int price) {
        this.id = id;
        this.ean = ean;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


}
