package com.example.housewastecollection;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PaymentManagementActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btnAddPayment;
    DatabaseHelper dbHelper;
    List<Payment> paymentList;
    List<String> customerNames;
    PaymentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_management);

        recyclerView = findViewById(R.id.recyclerViewPayments);
        btnAddPayment = findViewById(R.id.btnAddPayment);
        dbHelper = new DatabaseHelper(this);

        paymentList = new ArrayList<>();
        customerNames = new ArrayList<>();

        adapter = new PaymentAdapter(this, paymentList, customerNames, new PaymentAdapter.OnPaymentActionListener() {
            @Override
            public void onEdit(Payment payment) {
                Intent intent = new Intent(PaymentManagementActivity.this, AddPaymentActivity.class);
                intent.putExtra("payment_id", payment.getId());
                startActivity(intent);
            }

            @Override
            public void onDelete(Payment payment) {
                new AlertDialog.Builder(PaymentManagementActivity.this)
                        .setTitle("Delete Payment")
                        .setMessage("Are you sure you want to delete this payment?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            deletePayment(payment.getId());
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnAddPayment.setOnClickListener(v -> {
            startActivity(new Intent(this, AddPaymentActivity.class));
        });

        loadPayments();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPayments();
    }

    private void loadPayments() {
        paymentList.clear();
        customerNames.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_PAYMENT, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.PAYMENT_ID));
                int customerId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.PAYMENT_CUSTOMER_ID));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.PAYMENT_AMOUNT));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.PAYMENT_TYPE));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.PAYMENT_DATE));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.PAYMENT_STATUS));

                paymentList.add(new Payment(id, customerId, amount, type, date, status));

                // Fetch customer name
                Cursor c = db.rawQuery("SELECT full_name FROM " + DatabaseHelper.TABLE_CUSTOMER + " WHERE id = ?", new String[]{String.valueOf(customerId)});
                if (c.moveToFirst()) {
                    customerNames.add(c.getString(0));
                } else {
                    customerNames.add("Unknown");
                }
                c.close();
            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter.notifyDataSetChanged();
    }

    private void deletePayment(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deleted = db.delete(DatabaseHelper.TABLE_PAYMENT, DatabaseHelper.PAYMENT_ID + "=?", new String[]{String.valueOf(id)});
        if (deleted > 0) {
            Toast.makeText(this, "Payment deleted", Toast.LENGTH_SHORT).show();
            loadPayments();
        } else {
            Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show();
        }
    }
}
