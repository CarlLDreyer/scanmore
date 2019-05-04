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

import java.util.List;

public class ScannedProdAdapter extends RecyclerView.Adapter<ScannedProdAdapter.ScannedViewHolder> {
    private List<Product> scannedProducts;
    private ScanActivity sa = ScanActivity.getInstance();

    public static class ScannedViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public ImageButton removeItemButton;

        public ScannedViewHolder(View itemView) {

            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.scanned_product_name);
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
        // Get the data model based on position
        Product product = scannedProducts.get(i);
        // Set item views based on your views and data model
        TextView textView = viewHolder.nameTextView;
        textView.setText(product.getName());
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
    public ScannedProdAdapter(List<Product> products) {
        scannedProducts = products;
    }


}
