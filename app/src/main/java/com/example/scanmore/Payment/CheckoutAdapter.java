package com.example.scanmore.Payment;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanmore.Database.Product;
import com.example.scanmore.R;
import com.example.scanmore.Scanner.ScanActivity;

import com.example.scanmore.Utils.Pair;


import java.util.ArrayList;


public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder> {
    private ArrayList<Pair<Product, Integer>> checkoutProducts;
    private ScanActivity sa = ScanActivity.getInstance();
    private CheckoutActivity ca = CheckoutActivity.getInstance();

    public static class CheckoutViewHolder extends RecyclerView.ViewHolder {


        public TextView checkoutProductQuantity;
        public TextView checkoutProductName;
        public TextView checkoutProductPrice;


        public CheckoutViewHolder(View itemView) {

            super(itemView);

            checkoutProductQuantity = (TextView) itemView.findViewById(R.id.checkout_product_quantity);
            checkoutProductName = (TextView) itemView.findViewById(R.id.checkout_product_name);
            checkoutProductPrice = (TextView) itemView.findViewById(R.id.checkout_product_price);

        }
    }
    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View productView = inflater.inflate(R.layout.item_checkout_product, viewGroup, false);

        // Return a new holder instance
        CheckoutViewHolder viewHolder = new CheckoutViewHolder(productView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder viewHolder, final int i) {
        Pair productPair = checkoutProducts.get(i);
        Product product = productPair.getProduct();
        final int quantity = productPair.getQuantity();

        TextView quantityView = viewHolder.checkoutProductQuantity;
        TextView nameView = viewHolder.checkoutProductName;
        TextView priceView = viewHolder.checkoutProductPrice;

        quantityView.setText(String.valueOf(quantity));
        nameView.setText(product.getName());
        priceView.setText(String.valueOf(product.getPrice() * quantity) + " SEK");


        if (i %2 == 0) {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#fafafa"));
        } else {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Override
    public int getItemCount() {
        try{
            return checkoutProducts.size();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        return 0;
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public CheckoutAdapter(ArrayList<Pair<Product, Integer>> products) {
        checkoutProducts = products;
    }

}
