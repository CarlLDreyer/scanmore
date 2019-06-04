package com.example.scanmore.Payment.PaymentCheckout;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.scanmore.Database.CreditCard;
import com.example.scanmore.Database.DatabaseHandler;
import com.example.scanmore.Payment.CheckoutActivity;
import com.example.scanmore.R;
import com.example.scanmore.Scanner.ScanActivity;
import com.example.scanmore.Scanner.ScannedList.ScannedListActivity;
import com.google.zxing.client.android.Intents;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class PaymentMethodAdapter extends ArrayAdapter<PaymentMethodItem> {
    private Context context;
    private List<PaymentMethodItem> paymentMethods;
    private CheckoutActivity ca = CheckoutActivity.getInstance();
    private ScanActivity sa = ScanActivity.getInstance();
    private ScannedListActivity sla = ScannedListActivity.getInstance();
    private Dialog swishDialog;
    private Dialog swishDialogComplete;
    private Dialog CCDialog;
    private Dialog CCSuccessDialog;
    private DatabaseHandler dbHandler = new DatabaseHandler(getContext());
    private int total;

    public PaymentMethodAdapter(Context context, List<PaymentMethodItem> paymentMethods){
        super(context, R.layout.item_payment_method, paymentMethods);
        this.context = context;
        this.paymentMethods = paymentMethods;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_payment_method, parent, false);
        ImageView methodIcon = (ImageView) view.findViewById(R.id.payment_type);
        methodIcon.setImageResource(paymentMethods.get(position).getPhoto());
        TextView methodName = (TextView) view.findViewById(R.id.payment_name);
        methodName.setText(paymentMethods.get(position).getName());
        Button deletePayment = (Button) view.findViewById(R.id.delete_payment_method);
        deletePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ca.openCardDeletionDialog(position);
            }
        });
        try{
            total = sa.getTotalPrice();
        }
        catch(NullPointerException e) {e.printStackTrace();}
        swishDialog = new Dialog(ca);
        swishDialogComplete = new Dialog(ca);
        CCDialog = new Dialog(ca);
        CCSuccessDialog = new Dialog(ca);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int total = 0;
                try{ total = sa.getTotalPrice(); }
                catch(NullPointerException e){e.printStackTrace();}

                if(total != 0 && !isCreditCard(paymentMethods.get(position).getName())){
                    openSwishDialog();
                }
                else if(total != 0 && isCreditCard(paymentMethods.get(position).getName())){
                    openCCDialog(position);
                }
                else {
                    ca.openNoPaymentDialog();
                }
            }
        });
        return view;

    }

    private void openSwishDialog(){
        swishDialog.setContentView(R.layout.swish_popup);
        swishDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        swishDialog.setCanceledOnTouchOutside(false);
        TextView swishAmount = (TextView) swishDialog.findViewById(R.id.swish_amount);
        swishAmount.setText(total + " SEK");
        Button payButton = (Button) swishDialog.findViewById(R.id.pay_button);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swishDialog.dismiss();
                sa.emptyShoppingCart();
                sa.updateTotalPrice();
                if(sla != null){
                    sla.initTextViews();
                }
                ca.updateTotalPrice();
                openSwishDialogSuccess();
            }
        });
        Button cancelButton = (Button) swishDialog.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swishDialog.dismiss();
            }
        });

        swishDialog.show();
    }

    private void openSwishDialogSuccess(){
        swishDialogComplete.setContentView(R.layout.swish_popup_success);
        swishDialogComplete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        swishDialogComplete.setCanceledOnTouchOutside(false);
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        TextView dateSwish = (TextView) swishDialogComplete.findViewById(R.id.current_date_swish);
        dateSwish.setText(currentDateTimeString);
        TextView swishAmountSuccess = (TextView) swishDialogComplete.findViewById(R.id.swish_amount_success);
        swishAmountSuccess.setText(total + " SEK");
        Button dismissSuccess = (Button) swishDialogComplete.findViewById(R.id.dismiss_success);
        dismissSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swishDialogComplete.dismiss();
            }
        });
        swishDialogComplete.show();
    }

    private void openCCDialog(int pos){
        CreditCard cc = dbHandler.getCreditCard(paymentMethods.get(pos).getName());

        CCDialog.setContentView(R.layout.cc_popup);
        CCDialog.setCanceledOnTouchOutside(false);
        CCSuccessDialog.setContentView(R.layout.cc_popup_success);
        CCSuccessDialog.setCanceledOnTouchOutside(false);
        ImageView test = (ImageView) CCDialog.findViewById(R.id.active_card_type_two);
        Button dismiss_cc_popup = (Button) CCDialog.findViewById(R.id.dismiss_confirm);
        TextView pay_with_number = (TextView) CCDialog.findViewById(R.id.cc_number_confirm);
        pay_with_number.setText(paymentMethods.get(pos).getName());

        TextView total_pay = (TextView) CCDialog.findViewById(R.id.total_text_confirm);
        int total = sa.getTotalPrice();
        total_pay.setText(Integer.toString(total)+ "kr");

        CCDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CCDialog.show();

        if(cc.getCardType() == R.drawable.ic_visa){
            test.setImageResource(R.drawable.ic_visa);
        }
        else{
            test.setImageResource(R.drawable.ic_mastercard);
        }

        dismiss_cc_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CCDialog.dismiss();
            }
        });

        Button confirm_pay = (Button) CCDialog.findViewById(R.id.pay_button_confirm);
        confirm_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CCDialog.dismiss();
                sa.emptyShoppingCart();
                sa.updateTotalPrice();
                if(sla != null){
                    sla.initTextViews();
                }
                ca.updateTotalPrice();
                CCSuccessDialog.show();
            }

        });

        Button dismiss_button = (Button) CCSuccessDialog.findViewById(R.id.dismiss_success_cc);
        dismiss_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CCSuccessDialog.dismiss();
            }
        });

        Button done_button = (Button) CCSuccessDialog.findViewById(R.id.back_button);
        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CCSuccessDialog.dismiss();
            }
        });

    }

    public boolean isCreditCard(String number){
        return number.length() > 10;
    }
}
