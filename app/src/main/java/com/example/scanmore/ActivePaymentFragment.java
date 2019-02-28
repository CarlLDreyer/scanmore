package com.example.scanmore;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scanmore.Payment.PaymentUtils.CreditCardUtils;
import com.example.scanmore.Payment.PaymentUtils.FontTypeChange;
import com.example.scanmore.R;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;




/**
 * A simple {@link Fragment} subclass.
 */
public class ActivePaymentFragment extends Fragment {

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
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.activity_active_payment, container, false);
        ButterKnife.bind(this, view);
        fontTypeChange=new FontTypeChange(getActivity());
        //activeCardNumber.setTypeface(fontTypeChange.get_fontface(3));
        //activeCardName.setTypeface(fontTypeChange.get_fontface(3));
        activeCardNumber.setText(cardNumber);
        activeCardName.setText(cardName);
        activeCardValidity.setText(cardValidity);
        setCardType(cardType);
        /*activeCardNumber.setText("5226 XXXX XXXX 1222");
        activeCardName.setText("Calle Larsson");
        activeCardValidity.setText("07/20");
        setCardType(VISA); */

        return view;
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


}
