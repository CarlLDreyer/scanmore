package com.example.scanmore.Scanner.ScannedList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanmore.Database.Product;
import com.example.scanmore.LoginActivity;
import com.example.scanmore.Payment.CheckoutActivity;
import com.example.scanmore.R;
import com.example.scanmore.Scanner.ScanActivity;
import com.example.scanmore.SignupActivity;
import com.example.scanmore.Utils.Pair;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.ArrayList;

public class ScannedListActivity extends AppCompatActivity {
    private RecyclerView scannedProducts;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ScanActivity sa;
    private ArrayList<Pair<Product, Integer>> scannedList;
    private static ScannedListActivity sInstance = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_list);
        setupToolbar();
        sInstance = this;
        sa = ScanActivity.getInstance();
        scannedProducts = (RecyclerView) findViewById(R.id.scanned_items);

        layoutManager = new LinearLayoutManager(this);
        scannedProducts.setLayoutManager(layoutManager);
        scannedList = sa.getShoppingProducts();
        mAdapter = new ScannedProdAdapter(scannedList);
        scannedProducts.setAdapter(mAdapter);
        initTextViews();

        Button checkout = (Button) findViewById(R.id.checkout_button);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ScannedListActivity.this, CheckoutActivity.class);
                startActivity(i);
            }
        });

    }

    public static ScannedListActivity getInstance() {
        return sInstance ;
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initTextViews(){
        TextView totalItems = (TextView) findViewById(R.id.scanned_product_quantity);
        //totalItems.setText();
        int total = 0;
        for(Pair p : scannedList){
            total += p.getQuantity();
        }
        totalItems.setText(String.valueOf(total) + " Items");

        TextView totalPrice = (TextView) findViewById(R.id.scanned_product_price);
        totalPrice.setText(String.valueOf(sa.getTotalPrice()) + " SEK");
    }

    public void removeSingleItem(int itemPosition){
        final Pair p = scannedList.get(itemPosition);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Remove product");
        alert.setMessage("Are you sure you want to remove " + p.getProduct().getName() + "?");
        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                int oldIndexP = scannedList.indexOf(p);
                scannedList.remove(p);
                mAdapter.notifyItemRemoved(oldIndexP);
                mAdapter.notifyDataSetChanged();
                //sa.updateTotalPrice();
                initTextViews();
            }
        });
        alert.setNegativeButton(R.string.avbryt, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    public void removeMultipleItems(int itemPos){
        final Pair p = scannedList.get(itemPos);
        final Dialog d = new Dialog(ScannedListActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.delete_amount_dialog);
        Button okButton = (Button) d.findViewById(R.id.button_ok);
        Button cancelButton = (Button) d.findViewById(R.id.button_avbryt);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.number_picker);
        int max = p.getQuantity();
        np.setMaxValue(max); // max value 100
        np.setMinValue(1);   // min value 0
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {

            }
        });

        okButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                int valueToRemove = np.getValue();
                if(p.getQuantity() - valueToRemove == 0){
                    int oldIndexP = scannedList.indexOf(p);
                    scannedList.remove(p);
                    mAdapter.notifyItemRemoved(oldIndexP);
                    mAdapter.notifyDataSetChanged();
                    //sa.updateTotalPrice();
                    initTextViews();
                    d.dismiss();
                }
                else{
                    int newQuantity = p.getQuantity() - valueToRemove;
                    p.setQuantity(newQuantity);
                    mAdapter.notifyDataSetChanged();
                    initTextViews();
                    d.dismiss();

                }

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();
    }

    }




