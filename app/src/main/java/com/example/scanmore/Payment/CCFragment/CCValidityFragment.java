package com.example.scanmore.Payment.CCFragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.scanmore.CardFrontFragment;
import com.example.scanmore.PaymentMethodActivity;
import com.example.scanmore.R;
import com.example.scanmore.Payment.PaymentUtils.CreditCardEditText;
import com.example.scanmore.Payment.PaymentUtils.CreditCardExpiryTextWatcher;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CCValidityFragment extends Fragment {


    @BindView(R.id.et_validity)
    CreditCardEditText et_validity;
    TextView tv_validity;

    PaymentMethodActivity activity;
    CardFrontFragment cardFrontFragment;

    public CCValidityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ccvalidity, container, false);
        ButterKnife.bind(this, view);

        activity = (PaymentMethodActivity) getActivity();
        cardFrontFragment = activity.cardFrontFragment;


        tv_validity = cardFrontFragment.getValidity();
        et_validity.addTextChangedListener(new CreditCardExpiryTextWatcher(et_validity, tv_validity));

        et_validity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    if (activity != null) {
                        activity.nextClick();
                        return true;
                    }

                }
                return false;
            }
        });

        et_validity.setOnBackButtonListener(new CreditCardEditText.BackButtonListener() {
            @Override
            public void onBackClick() {
                if(activity!=null)
                    activity.onBackPressed();
            }
        });

        et_validity.requestFocus();

        return view;
    }

    public String getValidity()
    {
        if(et_validity!=null)
            return et_validity.getText().toString().trim();

        return null;
    }

}
