package com.example.scanmore;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.scanmore.Scanner.ScanActivity;

import java.text.DateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SwishFragment extends Fragment implements View.OnClickListener {

    private String phoneNumber;
    private Dialog swishDialog;
    private Dialog swishDialogComplete;
    private ScanActivity sc = ScanActivity.getInstance();
    int total;

    @BindView(R.id.swish_active_number)TextView swishNumber;

    PayActivity pa = PayActivity.getInstance();
    SwishActivity sa = SwishActivity.getInstance();


    public SwishFragment() {
        // Required empty public constructor
    }

    public SwishFragment(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.swish_fragment, container, false);
        swishDialog = new Dialog(pa);
        swishDialogComplete = new Dialog(pa);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int total = 0;
                try{ total = sc.getTotalPrice(); }
                catch(NullPointerException e){e.printStackTrace();}

                if(total != 0){
                    openSwishDialog();
                }
                else {
                    pa.openNoPaymentDialog();
                }
            }
        });
        ButterKnife.bind(this, view);
        Button deletionButton = (Button) view.findViewById(R.id.delete_credit_card);
        if(sa != null){
            EditText editNumber = sa.getPhoneNumber();
            String pNumber = editNumber.getText().toString();
            swishNumber.setText(pNumber);
        }

        deletionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pa.openCardDeletionDialog();
            }
        });
        try{
            total = sc.getTotalPrice();
        }
        catch(NullPointerException e) {e.printStackTrace();}
      return view;
    }
    public String getPhoneNumberString(){
        return phoneNumber;
    }

    @Override
    public void onClick(View view) {

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
                sc.emptyShoppingCart();
                sc.updateTotalPrice();
                pa.updateTotalPrice();
                swishDialogComplete.dismiss();
            }
        });
        swishDialogComplete.show();
    }
}
