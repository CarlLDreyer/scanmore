package com.example.scanmore.Payment.PaymentCheckout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.scanmore.R;

import java.util.List;

public class PaymentMethodAdapter extends ArrayAdapter<PaymentMethodItem> {
    private Context context;
    private List<PaymentMethodItem> paymentMethods;

    public PaymentMethodAdapter(Context context, List<PaymentMethodItem> paymentMethods){
        super(context, R.layout.item_payment_method, paymentMethods);
        this.context = context;
        this.paymentMethods = paymentMethods;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_payment_method, parent, false);
        ImageView methodIcon = (ImageView) view.findViewById(R.id.payment_type);
        methodIcon.setImageResource(paymentMethods.get(position).getPhoto());
        TextView methodName = (TextView) view.findViewById(R.id.payment_name);
        methodName.setText(paymentMethods.get(position).getName());
        return view;
    }
}
