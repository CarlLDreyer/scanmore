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
public class CardFrontFragment extends Fragment {

    private static final int VISA  = CreditCardUtils.VISA;
    private static final int MASTERCARD  = CreditCardUtils.MASTERCARD;
    private static final int NONE  = CreditCardUtils.NONE;

    @BindView(R.id.tv_card_number)TextView tvNumber;
    @BindView(R.id.tv_member_name)TextView tvName;
    @BindView(R.id.tv_validity)TextView tvValidity;
    @BindView(R.id.ivType)ImageView ivType;

    FontTypeChange fontTypeChange;

    public CardFrontFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_card_front, container, false);
        ButterKnife.bind(this, view);
        fontTypeChange=new FontTypeChange(getActivity());
        tvNumber.setTypeface(fontTypeChange.get_fontface(3));
        tvName.setTypeface(fontTypeChange.get_fontface(3));

        return view;
    }

    public TextView getNumber()
    {
        return tvNumber;
    }
    public TextView getName()
    {
        return tvName;
    }
    public TextView getValidity()
    {
        return tvValidity;
    }

    public ImageView getCardType()
    {
        return ivType;
    }


    public void setCardType(int type)
    {
        switch(type)
        {
            case VISA:
                ivType.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_visa));
                break;
            case MASTERCARD:
                ivType.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mastercard));
                break;
            case NONE:
                ivType.setImageResource(android.R.color.transparent);
                break;

        }


    }


}
