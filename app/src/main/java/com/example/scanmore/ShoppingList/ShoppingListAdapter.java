package com.example.scanmore.ShoppingList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.scanmore.R;

import com.example.scanmore.Database.Product;
import com.example.scanmore.Scanner.ScanActivity;
import com.example.scanmore.Utils.Pair;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShoppingListAdapter extends
        RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {
        ScanActivity sc = ScanActivity.getInstance();

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView productQuantity;
        public TextView productName;
        public TextView productPrice;
        public ImageButton removeItemButton;



        public ViewHolder(View itemView) {
            super(itemView);
            productQuantity = (TextView) itemView.findViewById(R.id.product_quantity);
            productName = (TextView) itemView.findViewById(R.id.product_name);
            productPrice = (TextView) itemView.findViewById(R.id.product_price);
            removeItemButton = (ImageButton) itemView.findViewById(R.id.remove_item_button);


        }
    }

    //private List<Product> productList;
    private ArrayList<Pair<Product, Integer>> productList;

    public ShoppingListAdapter(ArrayList<Pair<Product, Integer>> products) {
        productList = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View productView = inflater.inflate(R.layout.item_product, viewGroup, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(productView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        // Get the data model based on position
        //Product product = productList.get(i);
        Pair productPair = productList.get(i);
        Product product = productPair.getProduct();
        int quantity = productPair.getQuantity();
        // Set item views based on your views and data model

        TextView quantityView = viewHolder.productQuantity;
        TextView nameView = viewHolder.productName;
        TextView priceView = viewHolder.productPrice;



        quantityView.setText(String.valueOf(quantity));
        nameView.setText(product.getName());
        priceView.setText(String.valueOf(product.getPrice() * quantity) + " SEK");
        ImageButton b = viewHolder.removeItemButton;
        b.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
               sc.removeItemFromShoppingList(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

}
