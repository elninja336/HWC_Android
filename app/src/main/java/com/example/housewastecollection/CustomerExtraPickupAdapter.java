package com.example.housewastecollection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomerExtraPickupAdapter extends ArrayAdapter<ExtraPickup> {

    public CustomerExtraPickupAdapter(Context context, List<ExtraPickup> pickups) {
        super(context, 0, pickups);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ExtraPickup pickup = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);

        text1.setText("Date: " + pickup.getRequestDate());
        text2.setText("Status: " + pickup.getStatus() + ", Charge: TZS " + pickup.getExtraCharge());

        return convertView;
    }
}
