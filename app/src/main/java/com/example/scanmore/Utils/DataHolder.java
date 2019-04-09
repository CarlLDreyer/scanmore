package com.example.scanmore.Utils;

import com.example.scanmore.Database.Product;

import java.util.ArrayList;
import java.util.List;

public class DataHolder {
    final public ArrayList<Product> products = new ArrayList<>();
    final public int total = 0;

    private DataHolder() {}

    public static DataHolder getInstance() {
        if( instance == null ) {
            instance = new DataHolder();
        }
        return instance;
    }

    private static DataHolder instance;
}
