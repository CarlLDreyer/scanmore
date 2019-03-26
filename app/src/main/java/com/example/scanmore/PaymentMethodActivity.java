package com.example.scanmore;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.scanmore.Database.DatabaseHandler;
import com.example.scanmore.Payment.CCFragment.CCNameFragment;
import com.example.scanmore.Payment.CCFragment.CCNumberFragment;
import com.example.scanmore.Payment.CCFragment.CCSecureCodeFragment;
import com.example.scanmore.Payment.CCFragment.CCValidityFragment;
import com.example.scanmore.Payment.PaymentUtils.CreditCardUtils;
import com.example.scanmore.Payment.PaymentUtils.ViewPagerAdapter;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;


public class PaymentMethodActivity extends FragmentActivity implements FragmentManager.OnBackStackChangedListener {

    @BindView(R.id.btnNext)
    Button btnNext;

    public CardFrontFragment cardFrontFragment;
    public CardBackFragment cardBackFragment;

    //This is our viewPager
    private ViewPager viewPager;

    CCNumberFragment numberFragment;
    CCNameFragment nameFragment;
    CCValidityFragment validityFragment;
    CCSecureCodeFragment secureCodeFragment;



    int total_item;
    boolean backTrack = false;

    private boolean mShowingBack = false;

    String cardNumber, cardCVV, cardValidity, cardName;

    PayActivity pa = PayActivity.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        ButterKnife.bind(this);

        final DatabaseHandler databaseHandler = new DatabaseHandler(this);

        cardFrontFragment = new CardFrontFragment();
        cardBackFragment = new CardBackFragment();

        if (savedInstanceState == null) {
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, cardFrontFragment).commit();

        } else {
            mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
        }

        getFragmentManager().addOnBackStackChangedListener(this);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
        setupViewPager(viewPager);

        viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == total_item)
                    btnNext.setText("SUBMIT");
                else
                    btnNext.setText("NEXT");

                Log.d("track", "onPageSelected: " + position);

                if (position == total_item) {
                    flipCard();
                    backTrack = true;
                } else if (position == total_item - 1 && backTrack) {
                    flipCard();
                    backTrack = false;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = viewPager.getCurrentItem();
                cardName = nameFragment.getName();
                cardNumber = numberFragment.getCardNumber();
                cardValidity = validityFragment.getValidity();
                cardCVV = secureCodeFragment.getValue();

            if(pos == 0 && CreditCardUtils.isValid(cardNumber)){

                viewPager.setCurrentItem(pos + 1);
                }
                else if(pos == 1 && !TextUtils.isEmpty(cardName)){
                    viewPager.setCurrentItem(pos + 1);
                }
                else if(pos == 2 && CreditCardUtils.isValidDate(cardValidity)){
                     viewPager.setCurrentItem(pos + 1);
            }
            else if(pos == 3 && cardCVV.length() == 3){
                databaseHandler.insertCreditCard(cardNumber, cardName, cardValidity, CreditCardUtils.getCardType(cardNumber));
                pa.addFragmentCard(cardNumber, cardName, cardValidity, CreditCardUtils.getCardType(cardNumber));
                Intent intent = new Intent();
                intent.putExtra("key", "value");
                setResult(RESULT_OK, intent);
                finish();

                Toast.makeText(PaymentMethodActivity.this, "Card added successfully!", Toast.LENGTH_SHORT).show();
            }

            else{
                checkEntries();
            }

            }
        });

    }




    public void checkEntries() {
        cardName = nameFragment.getName();
        cardNumber = numberFragment.getCardNumber();
        cardValidity = validityFragment.getValidity();
        cardCVV = secureCodeFragment.getValue();

        if (TextUtils.isEmpty(cardName)) {
            Toast.makeText(PaymentMethodActivity.this, "Enter Valid Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cardNumber) || !CreditCardUtils.isValid(cardNumber.replace(" ",""))) {
            Toast.makeText(PaymentMethodActivity.this, "Enter Valid card number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cardValidity)||!CreditCardUtils.isValidDate(cardValidity)) {
            Toast.makeText(PaymentMethodActivity.this, "Enter correct validity", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cardCVV)||cardCVV.length()<3) {
            Toast.makeText(PaymentMethodActivity.this, "Enter valid security number", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(PaymentMethodActivity.this, "Your card is added", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackStackChanged() {
        mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        numberFragment = new CCNumberFragment();
        nameFragment = new CCNameFragment();
        validityFragment = new CCValidityFragment();
        secureCodeFragment = new CCSecureCodeFragment();
        adapter.addFragment(numberFragment);
        adapter.addFragment(nameFragment);
        adapter.addFragment(validityFragment);
        adapter.addFragment(secureCodeFragment);

        total_item = adapter.getCount() - 1;
        viewPager.setAdapter(adapter);

    }

    private void flipCard() {
        if (mShowingBack) {
            getFragmentManager().popBackStack();
            return;
        }
        // Flip to the back.
        //setCustomAnimations(int enter, int exit, int popEnter, int popExit)

        mShowingBack = true;

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)
                .replace(R.id.fragment_container, cardBackFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        int pos = viewPager.getCurrentItem();
        if (pos > 0) {
            viewPager.setCurrentItem(pos - 1);
        } else
            super.onBackPressed();
    }

    public void nextClick() {
        btnNext.performClick();
    }

}
