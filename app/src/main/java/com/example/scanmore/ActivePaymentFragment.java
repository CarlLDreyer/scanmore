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

        Button deletionButton = (Button) view.findViewById(R.id.delete_credit_card);
        deletionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pa.openCardDeletionDialog();
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
        CCDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CCDialog.show();

        /*swishDialog.setContentView(R.layout.swish_popup);
        swishDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

        swishDialog.show(); */
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
