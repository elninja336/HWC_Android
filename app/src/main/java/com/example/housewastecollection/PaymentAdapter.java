package com.example.housewastecollection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    public interface OnPaymentActionListener {
        void onEdit(Payment payment);
        void onDelete(Payment payment);
    }

    private Context context;
    private List<Payment> paymentList;
    private List<String> customerNames;
    private OnPaymentActionListener listener;

    public PaymentAdapter(Context context, List<Payment> paymentList, List<String> customerNames, OnPaymentActionListener listener) {
        this.context = context;
        this.paymentList = paymentList;
        this.customerNames = customerNames;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_payment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Payment payment = paymentList.get(position);
        String customerName = customerNames.get(position);

        holder.tvCustomerName.setText("Name: " + customerName);
        holder.tvAmount.setText("Amount: " + payment.getAmount() + " TZS");
        holder.tvPaymentType.setText("Type: " + payment.getPaymentType());
        holder.tvPaymentDate.setText("Date: " + payment.getPaymentDate());
        holder.tvStatus.setText("Status: " + payment.getStatus());

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(payment);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(payment);
        });
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomerName, tvAmount, tvPaymentType, tvPaymentDate, tvStatus;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvPaymentType = itemView.findViewById(R.id.tvPaymentType);
            tvPaymentDate = itemView.findViewById(R.id.tvPaymentDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
