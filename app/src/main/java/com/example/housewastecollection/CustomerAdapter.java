package com.example.housewastecollection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {

    public interface OnCustomerActionListener {
        void onEdit(Customer customer);
        void onDelete(Customer customer);
    }

    private Context context;
    private List<Customer> customerList;
    private OnCustomerActionListener listener;

    public CustomerAdapter(Context context, List<Customer> customerList, OnCustomerActionListener listener) {
        this.context = context;
        this.customerList = customerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.tvFullName.setText("Name: " + customer.getFullName());
        holder.tvEmail.setText("Email: " + customer.getEmail());
        holder.tvPhone.setText("Phone: " + customer.getPhone());
        holder.tvHouseNo.setText("House No: " + customer.getHouseNo());
        holder.tvDistrict.setText("District: " + customer.getDistrict());

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(customer);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(customer);
        });
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public void updateList(List<Customer> newList) {
        customerList = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFullName, tvEmail, tvPhone, tvHouseNo, tvDistrict;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvHouseNo = itemView.findViewById(R.id.tvHouseNo);
            tvDistrict = itemView.findViewById(R.id.tvDistrict);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
