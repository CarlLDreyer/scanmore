package com.example.scanmore;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scanmore.Payment.PaymentUtils.CreditCardUtils;
import com.example.scanmore.Payment.PaymentUtils.FontTypeChange;
import com.example.scanmore.Scanner.ScanActivity;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;




/**
 * A simple {@link Fragment} subclass.
 */
public class ActivePaymentFragment extends Fragment implements View.OnClickListener {

    private PayActivity pa = PayActivity.getInstance();
    private ScanActivity sc = ScanActivity.getInstance();
    private Dialog CCDialog;
    private Dialog CCSuccessDialog;

    private String cardNumber;
    private String cardName;
    private String cardValidity;
    private int cardType;

    private static final int VISA  = CreditCardUtils.VISA;
    private static final int MASTERCARD  = CreditCardUtils.MASTERCARD;
    private static final int NONE  = CreditCardUtils.NONE;

    @BindView(R.id.active_card_number)TextView activeCardNumber;
    @BindView(R.id.active_card_name)TextView activeCardName;
    @BindView(R.id.active_card_validity)TextView activeCardValidity;
    @BindView(R.id.active_card_type)ImageView activeCardType;

    FontTypeChange fontTypeChange;

    public ActivePaymentFragment() {
        // Required empty public constructor
    }
    public ActivePaymentFragment(String cardNumber, String cardName, String cardValidity, int cardType) {
        this.cardNumber = cardNumber;
        this.cardName = cardName;
        this.cardValidity = cardValidity;
        this.cardType = cardType;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_active_payment, container, false);
        ButterKnife.bind(this, view);
        fontTypeChange=new FontTypeChange(getActivity());
        activeCardNumber.setText(cardNumber);
        activeCardName.setText(cardName);
        activeCardValidity.setText(cardValidity);
        setCardType(cardType);

        CCDialog = new Dialog(pa);

        CCSuccessDialog = new Dialog(pa);

        Button deletionButton = (Button) view.findViewById(R.id.delete_credit_card);
        deletionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sc.getTotalPrice() != 0){
                    openCCDialog();
                }
                else{
                    pa.openNoPaymentDialog();
                }
            }
        });
        return view;
    }

    private void openCCDialog(){
        CCDialog.setContentView(R.layout.cc_popup);
        CCDialog.setCanceledOnTouchOutside(false);
        CCSuccessDialog.setContentView(R.layout.cc_popup_success);
        CCSuccessDialog.setCanceledOnTouchOutside(false);
        ImageView test = (ImageView) CCDialog.findViewById(R.id.active_card_type_two);
        Button dismiss_cc_popup = (Button) CCDialog.findViewById(R.id.dismiss_confirm);
        TextView pay_with_number = (TextView) CCDialog.findViewById(R.id.cc_number_confirm);
        pay_with_number.setText(cardNumber);
        TextView pay_with_name = (TextView) CCDialog.findViewById(R.id.cc_name_confirm);
        pay_with_name.setText(cardName);

        TextView total_pay = (TextView) CCDialog.findViewById(R.id.total_text_confirm);
        int total = sc.getTotalPrice();
        total_pay.setText(Integer.toString(total)+ "kr");

        CCDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CCDialog.show();

        if(cardType == VISA){
            test.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_visa));
        }
        else{
            test.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mastercard));
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
                sc.emptyShoppingCart();
                sc.updateTotalPrice();
                pa.updateTotalPrice();
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
    public void setCardType(int type)
    {
        switch(type)
        {
            case VISA:
                activeCardType.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_visa));
                break;
            case MASTERCARD:
                activeCardType.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mastercard));

                break;
            case NONE:
                activeCardType.setImageResource(android.R.color.transparent);
                break;

        }
    }

    public String getCardNumber(){
        return cardNumber;
    }

    @Override
    public void onClick(View view) {

    }
}
