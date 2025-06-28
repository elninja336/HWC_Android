package com.example.housewastecollection;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AddPaymentActivity extends AppCompatActivity {

    Spinner spinnerCustomer, spinnerPaymentType, spinnerStatus;
    EditText etAmount;
    Button btnSave, btnCancel;

    DatabaseHelper dbHelper;
    ArrayList<String> customerNames;
    HashMap<String, Integer> customerMap;
    TextView tvPaymentDate;
    String currentDate;




    int editingId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

        dbHelper = new DatabaseHelper(this);
        spinnerCustomer = findViewById(R.id.spinnerCustomer);
        spinnerPaymentType = findViewById(R.id.spinnerPaymentType);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        etAmount = findViewById(R.id.etAmount);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        tvPaymentDate = findViewById(R.id.tvPaymentDate);

// Generate and display today's date
        currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        tvPaymentDate.setText("Date: " + currentDate);



        setupSpinners();
        loadCustomers();

        editingId = getIntent().getIntExtra("payment_id", -1);
        if (editingId != -1) {
            loadEditingPayment();
        }

        btnSave.setOnClickListener(v -> savePayment());

        btnCancel.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Cancel")
                    .setMessage("Are you sure you want to cancel?")
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void setupSpinners() {
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Select Type", "Monthly", "ExtraPickup"});
        spinnerPaymentType.setAdapter(typeAdapter);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Select Status", "Paid", "Pending"});
        spinnerStatus.setAdapter(statusAdapter);
    }

    private void loadCustomers() {
        customerNames = new ArrayList<>();
        customerMap = new HashMap<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, full_name FROM " + DatabaseHelper.TABLE_CUSTOMER, null);

        customerNames.add("Select Customer"); // Add default prompt

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                customerNames.add(name);
                customerMap.put(name, id);
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, customerNames);
        spinnerCustomer.setAdapter(adapter);
    }

    private void loadEditingPayment() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_PAYMENT + " WHERE id = ?", new String[]{String.valueOf(editingId)});
        if (cursor.moveToFirst()) {
            int customerId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.PAYMENT_CUSTOMER_ID));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.PAYMENT_AMOUNT));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.PAYMENT_TYPE));
            String status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.PAYMENT_STATUS));

            etAmount.setText(String.valueOf(amount));
            spinnerPaymentType.setSelection(type.equals("Monthly") ? 1 : 2);
            spinnerStatus.setSelection(status.equals("Paid") ? 1 : 2);

            for (int i = 1; i < customerNames.size(); i++) {
                if (customerMap.get(customerNames.get(i)) == customerId) {
                    spinnerCustomer.setSelection(i);
                    break;
                }
            }
        }
        cursor.close();
    }

    private void savePayment() {
        if (spinnerCustomer.getSelectedItemPosition() == 0 ||
                spinnerPaymentType.getSelectedItemPosition() == 0 ||
                spinnerStatus.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedCustomer = spinnerCustomer.getSelectedItem().toString();
        int customerId = customerMap.get(selectedCustomer);
        String type = spinnerPaymentType.getSelectedItem().toString();
        String status = spinnerStatus.getSelectedItem().toString();
        String amountStr = etAmount.getText().toString().trim();

        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.PAYMENT_CUSTOMER_ID, customerId);
        values.put(DatabaseHelper.PAYMENT_AMOUNT, amount);
        values.put(DatabaseHelper.PAYMENT_TYPE, type);
        values.put(DatabaseHelper.PAYMENT_STATUS, status);
        // registration_date handled automatically by DB (assumed)

        long result;
        if (editingId == -1) {
            result = db.insert(DatabaseHelper.TABLE_PAYMENT, null, values);
        } else {
            result = db.update(DatabaseHelper.TABLE_PAYMENT, values, "id=?", new String[]{String.valueOf(editingId)});
        }

        if (result != -1) {
            Toast.makeText(this, "Payment saved successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to save payment", Toast.LENGTH_SHORT).show();
        }
    }
}
