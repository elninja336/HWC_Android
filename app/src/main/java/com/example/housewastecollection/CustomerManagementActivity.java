package com.example.housewastecollection;

import android.content.DialogInterface;
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

public class CustomerManagementActivity extends AppCompatActivity {

    Button btnAddCustomer;
    RecyclerView recyclerView;
    CustomerAdapter adapter;
    DatabaseHelper dbHelper;
    List<Customer> customerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_management);

        btnAddCustomer = findViewById(R.id.btnAddCustomer);
        recyclerView = findViewById(R.id.recyclerViewCustomers);
        dbHelper = new DatabaseHelper(this);
        customerList = new ArrayList<>();

        adapter = new CustomerAdapter(this, customerList, new CustomerAdapter.OnCustomerActionListener() {
            @Override
            public void onEdit(Customer customer) {
                // Open AddCustomerActivity for editing, passing customer id
                Intent intent = new Intent(CustomerManagementActivity.this, AddCustomerActivity.class);
                intent.putExtra("customer_id", customer.getId());
                startActivity(intent);
            }

            @Override
            public void onDelete(Customer customer) {
                new AlertDialog.Builder(CustomerManagementActivity.this)
                        .setTitle("Delete Customer")
                        .setMessage("Are you sure you want to delete " + customer.getFullName() + "?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            deleteCustomer(customer.getId());
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnAddCustomer.setOnClickListener(v -> {
            startActivity(new Intent(CustomerManagementActivity.this, AddCustomerActivity.class));
        });

        loadCustomers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCustomers();
    }

    private void loadCustomers() {
        customerList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_CUSTOMER, null, null, null, null, null, DatabaseHelper.CUSTOMER_ID + " DESC");
        if (cursor.moveToFirst()) {
            do {
                Customer customer = new Customer();
                customer.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.CUSTOMER_ID)));
                customer.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CUSTOMER_FULL_NAME)));
                customer.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CUSTOMER_EMAIL)));
                customer.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CUSTOMER_PHONE)));
                customer.setHouseNo(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CUSTOMER_HOUSE_NO)));
                customer.setDistrict(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CUSTOMER_DISTRICT)));
                customer.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CUSTOMER_PASSWORD)));
                customer.setRegistrationDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CUSTOMER_REGISTRATION_DATE)));
                customerList.add(customer);
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.updateList(customerList);
    }

    private void deleteCustomer(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deleted = db.delete(DatabaseHelper.TABLE_CUSTOMER, DatabaseHelper.CUSTOMER_ID + "=?", new String[]{String.valueOf(id)});
        if (deleted > 0) {
            Toast.makeText(this, "Customer deleted", Toast.LENGTH_SHORT).show();
            loadCustomers();
        } else {
            Toast.makeText(this, "Failed to delete customer", Toast.LENGTH_SHORT).show();
        }
    }
}
