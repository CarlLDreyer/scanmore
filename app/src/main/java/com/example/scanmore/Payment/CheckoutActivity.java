package com.example.scanmore.Payment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanmore.Database.DatabaseHandler;
import com.example.scanmore.Database.Product;
import com.example.scanmore.Database.Swish;
import com.example.scanmore.Payment.PaymentCheckout.PaymentMethodAdapter;
import com.example.scanmore.Payment.PaymentCheckout.PaymentMethodItem;
import com.example.scanmore.PaymentMethodActivity;
import com.example.scanmore.R;
import com.example.scanmore.Scanner.ScanActivity;
import com.example.scanmore.SwishActivity;
import com.example.scanmore.Utils.Pair;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {
    //Variables
    private RecyclerView scannedProducts;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ScanActivity sa;
    public static CheckoutActivity sInstance;
    private ArrayList<Pair<Product, Integer>> checkoutList;
    private Dialog paymentMethodDialog;
    private ListView listviewPaymentMethods;
    private List<PaymentMethodItem> paymentMethods;
    private List<PaymentMethodItem> existingPayments;
    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        setupToolbar();
        sa = ScanActivity.getInstance();
        scannedProducts = (RecyclerView) findViewById(R.id.checkout_items);
        sInstance = this;
        layoutManager = new LinearLayoutManager(this);
        scannedProducts.setLayoutManager(layoutManager);
        try{
            checkoutList = sa.getShoppingProducts();
        }
        catch(NullPointerException e){e.printStackTrace();}

        //checkoutList = sa.getShoppingProducts();
        mAdapter = new CheckoutAdapter(checkoutList);
        scannedProducts.setAdapter(mAdapter);
        dbHandler = new DatabaseHandler(this);

        initTextViews();
        paymentMethodDialog = new Dialog(this);
        paymentMethods = new ArrayList<PaymentMethodItem>();
        listviewPaymentMethods = (ListView) findViewById(R.id.payment_list_view);
        listviewPaymentMethods.setAdapter(new PaymentMethodAdapter(this, paymentMethods ));
        createActivePayments();


    }

    public void addPaymentMethod(String name, int icon){
        PaymentMethodItem payment = new PaymentMethodItem(name, icon);
        paymentMethods.add(payment);
    }

    public void removePaymentMethod(int position){
        if(paymentMethods.get(position).getName().length() > 10){
            dbHandler.deleteCreditCard(paymentMethods.get(position).getName());
        }
        else{
            dbHandler.deleteSwish(paymentMethods.get(position).getName());
        }
        paymentMethods.remove(position);
        listviewPaymentMethods.invalidateViews();
    }

    public void createActivePayments(){
        List<PaymentMethodItem> swishList = dbHandler.getAllExistingSwish();
        for( PaymentMethodItem p : swishList){
            PaymentMethodItem newPmethod = new PaymentMethodItem(p.getName(), p.getPhoto());
            paymentMethods.add(newPmethod);
        }
        List<PaymentMethodItem> creditList = dbHandler.getAllExistingCreditCards();
        for( PaymentMethodItem p : creditList){
            PaymentMethodItem newP = new PaymentMethodItem(trimName(p.getName()), p.getPhoto());
            paymentMethods.add(newP);
        }
    }

    public void openNoPaymentDialog(){
        NoPaymentDialog noPaymentDialog = new NoPaymentDialog();
        noPaymentDialog.show(getSupportFragmentManager(), "No Payment Dialog");
    }

    public void openCardDeletionDialog(final int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Remove payment method");
        alert.setMessage("Are you sure you want to remove the selected payment method?");
        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                removePaymentMethod(position);
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

    public void openPaymentMethodDialog(View v){
        ImageButton closeDialogButton;
        ImageButton swish;
        ImageButton creditcard;
        paymentMethodDialog.setContentView(R.layout.payment_method_popup);
        paymentMethodDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        closeDialogButton = (ImageButton) paymentMethodDialog.findViewById(R.id.close_dialog_button);
        swish = (ImageButton) paymentMethodDialog.findViewById(R.id.swish_option);
        creditcard = (ImageButton) paymentMethodDialog.findViewById(R.id.credit_card_option);
        swish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), SwishActivity.class);
                startActivityForResult(myIntent, 0);
                paymentMethodDialog.dismiss();
            }
        });
        creditcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), PaymentMethodActivity.class);
                startActivityForResult(myIntent, 0);
                paymentMethodDialog.dismiss();
            }
        });
        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                paymentMethodDialog.dismiss();
            }
        });
        paymentMethodDialog.show();
    }


    public static CheckoutActivity getInstance() {
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
        TextView totalPrice = (TextView) findViewById(R.id.checkout_price);
        try{
            totalPrice.setText(String.valueOf(sa.getTotalPrice()) + " SEK");
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        TextView priceText = (TextView) findViewById(R.id.pay_with);

        try{
            if(paymentMethods.size() != 0){
                priceText.setText("Pay with");
            }
            else{
                priceText.setText("Please add a payment method!");
            }
        }
        catch (NullPointerException e){ e.printStackTrace(); priceText.setText("Please add a payment method!"); }
    }

    public void updateTotalPrice(){
        TextView totalPrice = (TextView) findViewById(R.id.checkout_price);
        try{
            totalPrice.setText(String.valueOf(sa.getTotalPrice()) + " SEK");
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
    }
    public String trimName(String s){
        String[] sp = s.split(" ");
        return sp[0]+ " **** **** " + sp[3];
    }

}
