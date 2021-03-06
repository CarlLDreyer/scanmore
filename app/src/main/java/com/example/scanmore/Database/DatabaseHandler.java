package com.example.scanmore.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.database.Cursor;

import com.example.scanmore.Payment.PaymentCheckout.PaymentMethodItem;
import com.example.scanmore.R;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "jenny";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Product.CREATE_TABLE);
        db.execSQL(CreditCard.CREATE_TABLE);
        db.execSQL(Swish.CREATE_TABLE);
        db.execSQL(User.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Product.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CreditCard.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Swish.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    public void deleteAllCreditCards(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + CreditCard.TABLE_NAME);
    }


    public void deleteCreditCard(String number) {
        //Open the database
        SQLiteDatabase database = this.getWritableDatabase();

        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        database.execSQL("DELETE FROM " + CreditCard.TABLE_NAME + " WHERE " + CreditCard.COLUMN_CARD_NUMBER + "= '" + number + "'");

        //Close the database
        database.close();
    }

    public void deleteSwish(String number) {
        //Open the database
        SQLiteDatabase database = this.getWritableDatabase();

        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        database.execSQL("DELETE FROM " + Swish.TABLE_NAME + " WHERE " + Swish.COLUMN_PHONE + "= '" + number + "'");

        //Close the database
        database.close();
    }

    public void deleteUser(String email){
        //Open the database
        SQLiteDatabase database = this.getWritableDatabase();

        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        database.execSQL("DELETE FROM " + User.TABLE_NAME + " WHERE " + User.COLUMN_EMAIL + "= '" + email + "'");

        //Close the database
        database.close();

    }

    public long insertSwish(String phoneNumber) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Swish.COLUMN_PHONE, phoneNumber);

        // insert row
        long id = db.insert(Swish.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
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

    public List<PaymentMethodItem> getAllExistingCreditCards(){
        List<PaymentMethodItem> creditCards = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + CreditCard.TABLE_NAME + " ORDER BY " +
                CreditCard.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //CreditCard creditCard = new CreditCard();
                PaymentMethodItem creditCard = new PaymentMethodItem();
                creditCard.setName(cursor.getString(cursor.getColumnIndex(CreditCard.COLUMN_CARD_NUMBER)));
                creditCard.setPhoto(cursor.getInt(cursor.getColumnIndex(CreditCard.COLUMN_CARD_TYPE)));
                creditCards.add(creditCard);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return creditCards;
    }
    public List<PaymentMethodItem> getAllExistingSwish(){
        List<PaymentMethodItem> swish = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Swish.TABLE_NAME + " ORDER BY " +
                Swish.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PaymentMethodItem s = new PaymentMethodItem();
                s.setName(cursor.getString(cursor.getColumnIndex(Swish.COLUMN_PHONE)));
                s.setPhoto(R.drawable.ic_swish);
                swish.add(s);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return swish;
    }


    public List<Swish> getAllSwish() {
        List<Swish> swishL = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Swish.TABLE_NAME + " ORDER BY " +
                Swish.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Swish s = new Swish();
                s.setId(cursor.getInt(cursor.getColumnIndex(Swish.COLUMN_ID)));
                s.setPhoneNumberString(cursor.getString(cursor.getColumnIndex(Swish.COLUMN_PHONE)));

                swishL.add(s);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return swishL;
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

    public User getUser(String email) {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + User.TABLE_NAME + " WHERE TRIM(email) = '"+email.trim()+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        User user = null;
        // looping through all rows and adding to list
        if (cursor != null)

            cursor.moveToFirst();

        if (cursor != null && cursor.getCount() > 0) {

            user = new User(cursor.getString(1), cursor.getString(2), cursor.getString(3));

        }

        // close db connection
        db.close();

        // return notes list
        return user;
    }

    public CreditCard getCreditCard(String number) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + CreditCard.TABLE_NAME+ " WHERE " + CreditCard.COLUMN_CARD_NUMBER + " = '" + number + "'";

        Cursor cursor = db.rawQuery(query, null);
        CreditCard cc = new CreditCard();
        if (cursor != null)

            cursor.moveToFirst();

        if (cursor != null && cursor.getCount() > 0) {
            cc.setCardName(cursor.getString(cursor.getColumnIndex(CreditCard.COLUMN_CARD_NAME)));
            cc.setCardNumber(number);
            cc.setCardValidity(cursor.getString(cursor.getColumnIndex(CreditCard.COLUMN_CARD_VALIDITY)));
            cc.setCardType(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CreditCard.COLUMN_CARD_TYPE))));

        }

        cursor.close();
        db.close();

        return cc;
    }

    public User queryUser(String email, String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        User user = null;

        Cursor cursor = db.query(User.TABLE_NAME, new String[]{User.COLUMN_ID,

                        User.COLUMN_EMAIL, User.COLUMN_PASSWORD}, User.COLUMN_EMAIL + "=? and " + User.COLUMN_PASSWORD + "=?",

                new String[]{email, password}, null, null, null, "1");

        if (cursor != null)

            cursor.moveToFirst();

        if (cursor != null && cursor.getCount() > 0) {

            user = new User(cursor.getString(1), cursor.getString(2));

        }

        // return user

        return user;

    }

    public void addUser2(User user) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(User.COLUMN_NAME, user.getName());

        values.put(User.COLUMN_EMAIL, user.getEmail());

        values.put(User.COLUMN_PASSWORD, user.getPassword());

        // Inserting Row

        db.insert(User.TABLE_NAME, null, values);

        db.close(); // Closing database connection

    }


    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + User.TABLE_NAME + " ORDER BY " +
                User.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();

                user.setId(cursor.getInt(cursor.getColumnIndex(User.COLUMN_ID)));

                //risky!
                user.setName(cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME)));

                user.setEmail(cursor.getString(cursor.getColumnIndex(User.COLUMN_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(User.COLUMN_PASSWORD)));

                users.add(user);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return users;
    }
}