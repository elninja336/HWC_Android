package com.example.housewastecollection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomerPaymentAdapter extends ArrayAdapter<Payment> {

    public CustomerPaymentAdapter(Context context, List<Payment> payments) {
        super(context, 0, payments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Payment payment = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);

        text1.setText("Amount: TZS " + payment.getAmount() + " (" + payment.getPaymentType() + ")");
        text2.setText("Status: " + payment.getStatus() + " on " + payment.getPaymentDate());

        return convertView;
    }
}
