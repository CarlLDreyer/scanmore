package com.example.scanmore.ShoppingList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.scanmore.R;

import com.example.scanmore.Database.Product;
import com.example.scanmore.ScanActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShoppingListAdapter extends
        RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {
    ScanActivity sc = ScanActivity.getInstance();
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public ImageButton removeItemButton;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.contact_name);
            removeItemButton = (ImageButton) itemView.findViewById(R.id.remove_item_button);


        }
    }

    private List<Product> productList;

    public ShoppingListAdapter(List<Product> products) {
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
        Product product = productList.get(i);

        // Set item views based on your views and data model
        TextView textView = viewHolder.nameTextView;
        textView.setText(product.getName() + product.getPrice() + " kr");
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
