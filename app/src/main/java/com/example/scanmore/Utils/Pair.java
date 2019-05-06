package com.example.scanmore.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.scanmore.Database.Product;
// Pair class
public class Pair<U, V>
{
    public  Product first;   	// first field of a Pair
    public  int second;  	// second field of a Pair

    // Constructs a new Pair with specified values
    public Pair(Product first, int second)
    {
        this.first = first;
        this.second = second;
    }

    public void setProduct(Product product){
        this.first = product;
    }

    public void setQuantity(int quantity){
        this.second = quantity;
    }
    public Product getProduct(){
        return first;
    }

    public int getQuantity(){
        return second;
    }


    @Override
    public String toString()
    {
        return "(" + first + ", " + second + ")";
    }


}

