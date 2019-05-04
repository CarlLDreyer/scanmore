package com.example.scanmore.Scanner.ScannedList;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanmore.Database.Product;
import com.example.scanmore.R;
import com.example.scanmore.Scanner.ScanActivity;

import java.util.ArrayList;

public class ScannedListActivity extends AppCompatActivity {
    private RecyclerView scannedProducts;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ScanActivity sa;
    private ArrayList<Product> scannedList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_list);
        setupToolbar();
        sa = ScanActivity.getInstance();
        scannedProducts = (RecyclerView) findViewById(R.id.scanned_items);

        layoutManager = new LinearLayoutManager(this);
        scannedProducts.setLayoutManager(layoutManager);
        scannedList = sa.getShoppingProducts();
        mAdapter = new ScannedProdAdapter(scannedList);
        scannedProducts.setAdapter(mAdapter);

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
}
