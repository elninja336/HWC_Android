// Updated AddCustomerActivity.java
// Added logic to check for "customer_id" and pre-fill values for editing
package com.example.housewastecollection;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddCustomerActivity extends AppCompatActivity {

    EditText etFullName, etEmail, etPhone, etHouseNo, etDistrict;
    Button btnSave, btnCancel;

    DatabaseHelper dbHelper;
    int customerId = -1; // New variable to track edit mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        dbHelper = new DatabaseHelper(this);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etHouseNo = findViewById(R.id.etHouseNo);
        etDistrict = findViewById(R.id.etDistrict);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCencel);

        // Check if we are editing
        customerId = getIntent().getIntExtra("customer_id", -1);
        if (customerId != -1) {
            loadCustomerData(customerId); // Load existing data if editing
        }

        btnSave.setOnClickListener(v -> saveCustomer());

        btnCancel.setOnClickListener(v -> {
            new AlertDialog.Builder(AddCustomerActivity.this)
                    .setTitle("Cancel")
                    .setMessage("Are you sure you want to cancel and go back?")
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void loadCustomerData(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_CUSTOMER, null, DatabaseHelper.CUSTOMER_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            etFullName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CUSTOMER_FULL_NAME)));
            etEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CUSTOMER_EMAIL)));
            etPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CUSTOMER_PHONE)));
            etHouseNo.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CUSTOMER_HOUSE_NO)));
            etDistrict.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CUSTOMER_DISTRICT)));
        }
        cursor.close();
    }

    private void saveCustomer() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String houseNo = etHouseNo.getText().toString().trim();
        String district = etDistrict.getText().toString().trim();

        if (fullName.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Name, email and Phone are required", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CUSTOMER_FULL_NAME, fullName);
        values.put(DatabaseHelper.CUSTOMER_EMAIL, email);
        values.put(DatabaseHelper.CUSTOMER_PHONE, phone);
        values.put(DatabaseHelper.CUSTOMER_HOUSE_NO, houseNo);
        values.put(DatabaseHelper.CUSTOMER_DISTRICT, district);

        if (customerId != -1) {
            // Update mode
            int rowsAffected = db.update(DatabaseHelper.TABLE_CUSTOMER, values, DatabaseHelper.CUSTOMER_ID + "=?", new String[]{String.valueOf(customerId)});
            if (rowsAffected > 0) {
                Toast.makeText(this, "Customer updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update customer", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Add new customer
            values.put(DatabaseHelper.CUSTOMER_REGISTRATION_DATE, String.valueOf(System.currentTimeMillis()));
            long result = db.insert(DatabaseHelper.TABLE_CUSTOMER, null, values);
            if (result != -1) {
                Toast.makeText(this, "Customer added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add customer", Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }
}
