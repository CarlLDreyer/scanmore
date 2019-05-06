package com.example.scanmore.Scanner.ScannedList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanmore.Database.Product;
import com.example.scanmore.R;
import com.example.scanmore.Scanner.ScanActivity;
import com.example.scanmore.ShoppingList.ShoppingListAdapter;
import com.example.scanmore.Utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class ScannedProdAdapter extends RecyclerView.Adapter<ScannedProdAdapter.ScannedViewHolder> {
    private ArrayList<Pair<Product, Integer>> scannedProducts;
    private ScanActivity sa = ScanActivity.getInstance();

    public static class ScannedViewHolder extends RecyclerView.ViewHolder {

        public TextView scannedProductQuantity;
        public TextView scannedProductName;
        public TextView scannedProductPrice;
        public ImageButton removeItemButton;

        public ScannedViewHolder(View itemView) {

            super(itemView);

            scannedProductQuantity = (TextView) itemView.findViewById(R.id.scanned_product_quantity);
            scannedProductName = (TextView) itemView.findViewById(R.id.scanned_product_name);
            scannedProductPrice = (TextView) itemView.findViewById(R.id.scanned_product_price);
            removeItemButton = (ImageButton) itemView.findViewById(R.id.remove_item_button);

        }
    }
    @NonNull
    @Override
    public ScannedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View productView = inflater.inflate(R.layout.item_scanned_product, viewGroup, false);

        // Return a new holder instance
        ScannedViewHolder viewHolder = new ScannedViewHolder(productView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScannedViewHolder viewHolder, final int i) {
        Pair productPair = scannedProducts.get(i);
        Product product = productPair.getProduct();
        int quantity = productPair.getQuantity();

        TextView quantityView = viewHolder.scannedProductQuantity;
        TextView nameView = viewHolder.scannedProductName;
        TextView priceView = viewHolder.scannedProductPrice;


        quantityView.setText(String.valueOf(quantity));
        nameView.setText(product.getName());
        priceView.setText(String.valueOf(product.getPrice() * quantity) + " SEK");

        ImageButton b = viewHolder.removeItemButton;
        b.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                sa.removeItemFromShoppingList(i);
            }
        });
        if (i %2 == 0) {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#fafafa"));
        } else {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Override
    public int getItemCount() {
        return scannedProducts.size();
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public ScannedProdAdapter(ArrayList<Pair<Product, Integer>> products) {
        scannedProducts = products;
    }


}
