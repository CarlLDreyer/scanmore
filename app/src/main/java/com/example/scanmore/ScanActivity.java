package com.example.scanmore;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import android.widget.ToggleButton;

import com.example.scanmore.Database.DatabaseHandler;
import com.example.scanmore.Database.Product;
import com.example.scanmore.ShoppingList.ShoppingListAdapter;
import com.example.scanmore.Utils.DataHolder;
import com.google.zxing.Result;

import com.example.scanmore.BarcodeScanner.IViewFinder;
import com.example.scanmore.BarcodeScanner.ViewFinderView;
import com.example.scanmore.ZXing.ZXingScannerView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;


public class ScanActivity extends BaseScannerActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    public static boolean scanActive = false;
    private ArrayList<Product> shoppingProducts;
    private RecyclerView rvProducts;
    private ShoppingListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private static ScanActivity sInstance = null;
    private ToggleButton flash_toggle;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_scan);
        sInstance = this;
        shoppingProducts = DataHolder.getInstance().products;
        setupToolbar();
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this) {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };
        contentFrame.addView(mScannerView);
        final PulsatorLayout pulsator = (PulsatorLayout) findViewById(R.id.pulsator);
        final ImageButton Scanbutton = (ImageButton) findViewById(R.id.scanbutton);
        Scanbutton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Scanbutton.setAlpha(0.75f);
                        scanActive = true;
                        pulsator.start();
                        break;
                    case MotionEvent.ACTION_UP:
                        Scanbutton.setAlpha(1.0f);
                        scanActive = false;
                        pulsator.stop();
                        break;
                }
                return false;
            }
        });
        initShoppingList();
        TextView textView = findViewById(R.id.total_text);
        textView.setText("");
        flash_toggle = findViewById(R.id.toggleButton);
        flash_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mScannerView.getFlash())
                    mScannerView.setFlash(true);
                else{
                    mScannerView.setFlash(false);
                }
            }
        });
        Button button1 = (Button) findViewById(R.id.payButton);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), PayActivity.class);
                startActivityForResult(myIntent, 0);

            }
        });

        ImageButton shoppingListButton = (ImageButton) findViewById(R.id.shoppingList_button);
        shoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listIntent = new Intent(v.getContext(), ScannedListActivity.class);
                startActivity(listIntent);
            }
        });
    }

    public boolean getScanActive(){
        return scanActive;
    }

    // Getter to access Singleton instance
    public static ScanActivity getInstance() {
        return sInstance ;
    }



    @Override
    public void onResume() {
        super.onResume();
        updateTotalPrice();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        mScannerView.resumeCameraPreview(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
        mScannerView.stopCameraPreview();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mScannerView.stopCamera();
        mScannerView.stopCameraPreview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScannerView.stopCamera();
        mScannerView.stopCameraPreview();
    }



    @Override
    public void handleResult(Result rawResult) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        List<Product> products = databaseHandler.getAllProducts();
        for(Product p : products){
            if(p.getEan().equals(rawResult.getText())){
                addIntoShoppingList(p, adapter);
                v.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(ScanActivity.this);
            }
        }, 500);
    }

    private static class CustomViewFinderView extends ViewFinderView {
        public static final int MARK_TEXT_SIZE_SP = 40;
        public final Paint PAINT = new Paint();

        public CustomViewFinderView(Context context) {
            super(context);
            init();
        }

        public CustomViewFinderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            PAINT.setColor(Color.WHITE);
            PAINT.setAntiAlias(true);
            float textPixelSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    MARK_TEXT_SIZE_SP, getResources().getDisplayMetrics());
            PAINT.setTextSize(textPixelSize);
            setSquareViewFinder(true);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            //drawTradeMark(canvas);
        }

    }

    private void initShoppingList(){
        rvProducts = (RecyclerView) findViewById(R.id.shoppingList);
        adapter = new ShoppingListAdapter(shoppingProducts);
        rvProducts.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvProducts.setLayoutManager(linearLayoutManager);
    }

    private void addIntoShoppingList(Product product, ShoppingListAdapter adapter){

        shoppingProducts.add(product);
        adapter.notifyItemInserted(shoppingProducts.indexOf(product));
        linearLayoutManager.scrollToPosition(shoppingProducts.indexOf(product));
        updateTotalPrice();
    }

    public void removeItemFromShoppingList(int itemPosition){
        final Product p = shoppingProducts.get(itemPosition);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Remove product");
        alert.setMessage("Are you sure you want to remove " + p.getName() + "?");
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            int oldIndexP = shoppingProducts.indexOf(p);
            shoppingProducts.remove(p);
            adapter.notifyItemRemoved(oldIndexP);
            adapter.notifyDataSetChanged();
            updateTotalPrice();
            }
        });
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    public int getTotalPrice(){
        int tempTotal = 0;
        for(Product p : shoppingProducts){
            tempTotal += p.getPrice();
        }
        return tempTotal;
    }

    public void updateTotalPrice(){
        TextView total = (TextView) findViewById(R.id.total_text);
        if(getTotalPrice() != 0){
            total.setText("Total: " + getTotalPrice() + " kr");
        }
        else{
            total.setText("");
        }
    }

    public void emptyShoppingCart(){
        shoppingProducts.clear();
    }


}