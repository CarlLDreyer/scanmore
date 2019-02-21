package com.example.scanmore.Database;

public class Product {
    public static final String TABLE_NAME = "products";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EAN = "ean";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";


    private int id;
    private String ean;
    private String name;
    private String price;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_EAN + " TEXT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_PRICE + " TEXT"
                    + ")";

    public Product() {
    }

    public Product(int id, String ean, String name, String price) {
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


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
