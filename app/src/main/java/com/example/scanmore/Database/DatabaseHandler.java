package com.example.scanmore.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "products_db";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Product.CREATE_TABLE);
        db.execSQL(CreditCard.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Product.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CreditCard.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void deleteAllCreditCards(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + CreditCard.TABLE_NAME);
    }

    public long insertProduct(String ean, String name, int price) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Product.COLUMN_EAN, ean);
        values.put(Product.COLUMN_NAME, name);
        values.put(Product.COLUMN_PRICE, price);

        // insert row
        long id = db.insert(Product.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public long insertCreditCard(String cardNumber, String cardName, String cardValidity, int cardType) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(CreditCard.COLUMN_CARD_NUMBER, cardNumber);
        values.put(CreditCard.COLUMN_CARD_NAME, cardName);
        values.put(CreditCard.COLUMN_CARD_VALIDITY, cardValidity);
        values.put(CreditCard.COLUMN_CARD_TYPE, cardType);

        // insert row
        long id = db.insert(CreditCard.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }


    public Product getProduct(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Product.TABLE_NAME,
                new String[]{Product.COLUMN_ID, Product.COLUMN_EAN, Product.COLUMN_NAME, Product.COLUMN_PRICE},
                Product.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Product note = new Product(
                cursor.getInt(cursor.getColumnIndex(Product.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Product.COLUMN_EAN)),
                cursor.getString(cursor.getColumnIndex(Product.COLUMN_NAME)),
                cursor.getInt(cursor.getColumnIndex(Product.COLUMN_PRICE)));

        // close the db connection
        cursor.close();

        return note;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Product.TABLE_NAME + " ORDER BY " +
                Product.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex(Product.COLUMN_ID)));
                product.setEan(cursor.getString(cursor.getColumnIndex(Product.COLUMN_EAN)));
                product.setName(cursor.getString(cursor.getColumnIndex(Product.COLUMN_NAME)));
                product.setPrice(cursor.getInt(cursor.getColumnIndex(Product.COLUMN_PRICE)));


                products.add(product);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return products;
    }

    public List<CreditCard> getAllCreditCards() {
        List<CreditCard> creditCards = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + CreditCard.TABLE_NAME + " ORDER BY " +
                CreditCard.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CreditCard creditCard = new CreditCard();
                creditCard.setId(cursor.getInt(cursor.getColumnIndex(CreditCard.COLUMN_ID)));
                creditCard.setCardNumber(cursor.getString(cursor.getColumnIndex(CreditCard.COLUMN_CARD_NUMBER)));
                creditCard.setCardName(cursor.getString(cursor.getColumnIndex(CreditCard.COLUMN_CARD_NAME)));
                creditCard.setCardValidity(cursor.getString(cursor.getColumnIndex(CreditCard.COLUMN_CARD_VALIDITY)));
                creditCard.setCardType(cursor.getInt(cursor.getColumnIndex(CreditCard.COLUMN_CARD_TYPE)));


                creditCards.add(creditCard);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return creditCards;
    }
}