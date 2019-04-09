package com.example.scanmore;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.scanmore.Database.DatabaseHandler;
import com.example.scanmore.Database.CreditCard;
import com.example.scanmore.Database.Swish;
import com.example.scanmore.Payment.CCPreviewAdapter;
import com.example.scanmore.Payment.CreditCardDeletionDialog;
import com.example.scanmore.Payment.NoPaymentDialog;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

public class PayActivity extends AppCompatActivity {
    ScanActivity sc = ScanActivity.getInstance();
    private ViewPager active_viewPager;
    private CCPreviewAdapter adapter;
    private static PayActivity sInstance = null;
    DatabaseHandler databaseHandler;
    List<CreditCard> activeCreditCardList;
    List<Swish> activeSwishList;
    FragmentManager fm = this.getSupportFragmentManager();
    Dialog paymentMethodDialog;
    Dialog swishDialog;
    String phNumber ="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        setupToolbar();
        sInstance = this;
        //Initializing viewPager
        active_viewPager = (ViewPager) findViewById(R.id.active_viewpager);
        active_viewPager.setOffscreenPageLimit(1);
        setupViewPager(active_viewPager);
        databaseHandler = new DatabaseHandler(this);
        setupTextViews();
        activeCreditCardList = databaseHandler.getAllCreditCards();
        activeSwishList = databaseHandler.getAllSwish();
        createActiveCreditCards(activeCreditCardList);
        createActiveSwish(activeSwishList);
        paymentMethodDialog = new Dialog(this);
        swishDialog = new Dialog(this);
    }
    // CONTINUE WITH SWISH DIALOG WHEN SWISH FRAGMENT IS DONE
    public void openSwishDialog(View v){
        paymentMethodDialog.dismiss();
        swishDialog.setContentView(R.layout.swish_popup);
        swishDialog.show();
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

    public void openCardDeletionDialog() {
        CreditCardDeletionDialog dialog = new CreditCardDeletionDialog();
        dialog.show(getSupportFragmentManager(), "Card Deletion Dialog");
    }

    public void openNoPaymentDialog(){
        NoPaymentDialog noPaymentDialog = new NoPaymentDialog();
        noPaymentDialog.show(getSupportFragmentManager(), "No Payment Dialog");
    }

    public void deleteCard() {
        int currentPos = active_viewPager.getCurrentItem();
        if(adapter.getItem(currentPos) instanceof ActivePaymentFragment){
            ActivePaymentFragment ap = (ActivePaymentFragment) adapter.getItem(currentPos);
            databaseHandler.deleteCreditCard(ap.getCardNumber());
            adapter.removeFragment(currentPos);
            updatePaymentMethodsText();
        }
        else if(adapter.getItem(currentPos) instanceof SwishFragment){
            SwishFragment sf = (SwishFragment) adapter.getItem(currentPos);
            databaseHandler.deleteSwish(sf.getPhoneNumberString());
            adapter.removeFragment(currentPos);
            updatePaymentMethodsText();
        }

    }

    public static PayActivity getInstance() {
        return sInstance;
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

    private void setupTextViews() {
        int total = 0;
        try {
            total = sc.getTotalPrice();
        }
        catch(NullPointerException e){e.printStackTrace();
        }
        TextView toPay = (TextView) findViewById(R.id.toPay_text);
        toPay.setText("To pay");
        TextView total_price = (TextView) findViewById(R.id.total_text_pay);
        if (total != 0) {
            total_price.setText("" + total + "kr");
        } else {
            total_price.setText("0kr");
        }

        Button payment_method_button = (Button) findViewById(R.id.payment_method_button);
        payment_method_button.setText("ADD NEW PAYMENT METHOD");
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new CCPreviewAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    public void updateTotalPrice(){
        int total = sc.getTotalPrice();
        TextView total_price = (TextView) findViewById(R.id.total_text_pay);
        if (total != 0) {
            total_price.setText("" + total + "kr");
        } else {
            total_price.setText("0kr");
        }
    }

    public void createActiveCreditCards(List<CreditCard> creditCardList) {
        for (CreditCard c : creditCardList) {
            ActivePaymentFragment activeCardFragment = new ActivePaymentFragment(c.getCardNumber(), c.getCardName(), c.getCardValidity(), c.getCardType());
            adapter.addFrag(activeCardFragment, c.getCardNumber());
        }
        updatePaymentMethodsText();
    }
    public void createActiveSwish(List<Swish> swishList) {
        for (Swish s : swishList) {
            SwishFragment newSFrag = new SwishFragment(s.getPhoneNumber());
            adapter.addFrag(newSFrag, s.getPhoneNumber());
        }
        updatePaymentMethodsText();
    }



    public void addFragmentCard(String cardNumber, String cardName, String cardValidity, int cardType){
        ActivePaymentFragment newAPF = new ActivePaymentFragment(cardNumber, cardName, cardValidity, cardType);
        adapter.addFrag(newAPF, cardNumber);
        updatePaymentMethodsText();
        goToNewFragmentPos();
    }

    public void addFragmentSwish(EditText phoneNumber){
        phNumber = phoneNumber.getText().toString();
        SwishFragment newSwish = new SwishFragment(phNumber);
        adapter.addFrag(newSwish, phNumber);
        updatePaymentMethodsText();
        goToNewFragmentPos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
            }
        }
    }

    public void updatePaymentMethodsText() {
        TextView payment_method = (TextView) findViewById(R.id.payment_method);
        if (adapter.getCount() != 0) {
            payment_method.setText("Select payment method");
        } else {
            payment_method.setText("You have no active payment methods");
        }
    }

    public void goToFirstAdapterPos() {
        active_viewPager.setCurrentItem(0);
    }

    public void goToNewFragmentPos(){
        int newPos = adapter.getCount();
        active_viewPager.setCurrentItem(newPos);
    }

}