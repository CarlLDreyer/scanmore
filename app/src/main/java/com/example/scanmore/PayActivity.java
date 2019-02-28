package com.example.scanmore;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.scanmore.Database.DatabaseHandler;
import com.example.scanmore.Database.CreditCard;
import com.example.scanmore.Payment.PaymentUtils.ViewPagerAdapter;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


public class PayActivity extends AppCompatActivity {
    ScanActivity sc = ScanActivity.getInstance();
    private ViewPager active_viewPager;
    public CardFrontFragment cardFrontFragment;
    public ActivePaymentFragment activePaymentFragment;
    private ViewPagerAdapter adapter;
    private static PayActivity sInstance = null;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        setupToolbar();
        setupTextViews();
        sInstance = this;
        //Initializing viewPager
        active_viewPager = (ViewPager) findViewById(R.id.active_viewpager);
        active_viewPager.setOffscreenPageLimit(4);
        setupViewPager(active_viewPager);
        databaseHandler = new DatabaseHandler(this);

        List<CreditCard> activeCreditCardList = databaseHandler.getAllCreditCards();
        createActiveCreditCards(activeCreditCardList);


        Button deleteCC = (Button) findViewById(R.id.delete_credit_cards);
        deleteCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHandler.deleteAllCreditCards();
                adapter.notifyDataSetChanged();
                // ADD RECREATE ON DELETION

            }
        });

    }

    public static PayActivity getInstance() {
        return sInstance ;
    }
    private void refresh(){
        overridePendingTransition( 0, 0);
        this.recreate();
        overridePendingTransition( 0, 0);
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupTextViews(){
        int total = sc.getTotalPrice();
        TextView toPay = (TextView) findViewById(R.id.toPay_text);
        toPay.setText("To pay");
        TextView total_price = (TextView) findViewById(R.id.total_text_pay);
        if(total != 0){
            total_price.setText("" + total + "kr");
        }
        else{
            total_price.setText("0kr");
        }
        Button payment_method_button = (Button) findViewById(R.id.payment_method_button);
        payment_method_button.setText("ADD NEW PAYMENT METHOD");
        payment_method_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), PaymentMethodActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        /*activePaymentFragment = new ActivePaymentFragment();
        ActivePaymentFragment test2fragment = new ActivePaymentFragment("5225 5002 5000 2000", "Calle Johansson", "07/20", CreditCardUtils.MASTERCARD);

        adapter.addFragment(activePaymentFragment);
        adapter.addFragment(test2fragment); */

        //total_item = adapter.getCount() - 1;
        viewPager.setAdapter(adapter);

    }

    public void createActiveCreditCards(List<CreditCard> creditCardList){
        //id = total_item = adapter.getCount() - 1;
        for(CreditCard c : creditCardList){
            ActivePaymentFragment activeCardFragment = new ActivePaymentFragment(c.getCardNumber(), c.getCardName(), c.getCardValidity(), c.getCardType());
            adapter.addFragment(activeCardFragment);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if(resultCode == RESULT_OK) {
                adapter.notifyDataSetChanged();
                this.recreate();
            }
            }
    }
}
