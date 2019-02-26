package com.example.scanmore;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.scanmore.Database.DatabaseHandler;
import com.example.scanmore.Database.Product;
import com.example.scanmore.ShoppingList.ShoppingListAdapter;
import com.google.zxing.Result;

import com.example.scanmore.BarcodeScanner.IViewFinder;
import com.example.scanmore.BarcodeScanner.ViewFinderView;
import com.example.scanmore.ZXing.ZXingScannerView;

import java.util.ArrayList;
import java.util.List;


public class ScanActivity extends BaseScannerActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    public static boolean scanActive = false;
    private ArrayList<Product> shoppingProducts = new ArrayList<Product>();
    private RecyclerView rvProducts;
    private ShoppingListAdapter adapter;
    private int position = 0;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_scan);
        setupToolbar();
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this) {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };
        contentFrame.addView(mScannerView);
        Button Scanbutton = (Button) findViewById(R.id.scanbutton);
        Scanbutton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        System.out.println("HOLDING BUTTON DOWN");
                        scanActive = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        System.out.println("BUTTON RELESAED");
                        scanActive = false;
                        break;
                }
                return false;
            }
        });
        rvProducts = (RecyclerView) findViewById(R.id.shoppingList);
        adapter = new ShoppingListAdapter(shoppingProducts);
        rvProducts.setAdapter(adapter);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
    }

    public boolean getScanActive(){
        return scanActive;
    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();

    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mScannerView.stopCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        List<Product> products = databaseHandler.getAllProducts();
        for(Product p : products){
            if(p.getEan().equals(rawResult.getText())){
                // Add to shopping list
                Toast.makeText(getApplicationContext(), "Product: " + p.getName() + " Price: " + p.getPrice(), Toast.LENGTH_SHORT).show();

                addIntoShoppingList(p, adapter);

            }
        }

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(ScanActivity.this);
            }
        }, 2000);
    }

    private static class CustomViewFinderView extends ViewFinderView {
        public static final String MARK_TEXT = "Scan";
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

        private void drawTradeMark(Canvas canvas) {
            Rect framingRect = getFramingRect();
            float tradeMarkTop;
            float tradeMarkLeft;
            if (framingRect != null) {
                tradeMarkTop = framingRect.bottom + PAINT.getTextSize() + 10;
                tradeMarkLeft = framingRect.left + 50;
            } else {
                tradeMarkTop = 10;
                tradeMarkLeft = canvas.getHeight() - PAINT.getTextSize() - 10;
            }
            canvas.drawText(MARK_TEXT, tradeMarkLeft, tradeMarkTop, PAINT);
        }

    }

    /*private void initShoppingList(){
        RecyclerView rvProducts = (RecyclerView) findViewById(R.id.shoppingList);
        ShoppingListAdapter adapter = new ShoppingListAdapter(products);
        rvProducts.setAdapter(adapter);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
    } */

    private void addIntoShoppingList(Product product, ShoppingListAdapter adapter){
        shoppingProducts.add(product);
        adapter.notifyItemInserted(position);
        position++;

    }
    
}
