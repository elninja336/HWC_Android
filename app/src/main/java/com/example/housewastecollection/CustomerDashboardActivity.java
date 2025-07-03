package com.example.housewastecollection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CustomerDashboardActivity extends AppCompatActivity {

    TextView txtFullName, txtEmail, txtPhone, txtHouseNo, txtDistrict, txtRegDate;
    TextView txtCurrCollection, txtPrevCollection, txtNextCollection;
    TextView txtLastExtraPickup, txtLastPaymentStatus;

    Button btnEditProf, btnViewExtraPickup, btnAddExtraPickup;
    Button btnViewPayments, btnAddPayment;
    Button btnLogout;


    DatabaseHelper dbHelper;
    int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        dbHelper = new DatabaseHelper(this);
        customerId = getIntent().getIntExtra("customer_id", -1);
        btnLogout = findViewById(R.id.btnLogout);


        btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(CustomerDashboardActivity.this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Optional: Clear session or data

                        // Go to login activity
                        Intent intent = new Intent(CustomerDashboardActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
                        startActivity(intent);
                        finish(); // Close dashboard
                    })
                    .setNegativeButton("No", null)
                    .show();
        });



        // View bindings
        txtFullName = findViewById(R.id.txtFullName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtHouseNo = findViewById(R.id.txtHouseNo);
        txtDistrict = findViewById(R.id.txtDistrict);
//        txtRegDate = findViewById(R.id.txtRegDate);

        txtCurrCollection = findViewById(R.id.txtCurrCollection);
        txtPrevCollection = findViewById(R.id.txtPrevCollection);
        txtNextCollection = findViewById(R.id.txtNextCollection);

        txtLastExtraPickup = findViewById(R.id.txtLastExtraPickup);
        txtLastPaymentStatus = findViewById(R.id.txtLastPaymentStatus);

        btnEditProf = findViewById(R.id.btnEditProf);
        btnViewExtraPickup = findViewById(R.id.btnViewExtraPickup);
        btnAddExtraPickup = findViewById(R.id.btnAddExtraPickup);
        btnViewPayments = findViewById(R.id.btnViewPayments);
        btnAddPayment = findViewById(R.id.btnAddPayment);

        loadCustomerInfo();
        loadSchedule();
        loadLastExtraPickup();
        loadLastPayment();

        btnEditProf.setOnClickListener(v -> showEditProfileDialog());
        btnViewExtraPickup.setOnClickListener(v -> showExtraPickupsDialog());
        btnAddExtraPickup.setOnClickListener(v -> showAddExtraPickupDialog());
        btnViewPayments.setOnClickListener(v -> showPaymentsDialog());
        btnAddPayment.setOnClickListener(v -> showAddPaymentDialog());
    }

    private void loadCustomerInfo() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Customer WHERE id = ?", new String[]{String.valueOf(customerId)});
        if (cursor.moveToFirst()) {
            txtFullName.setText(cursor.getString(cursor.getColumnIndexOrThrow("full_name")));
            txtEmail.setText("Email: " + cursor.getString(cursor.getColumnIndexOrThrow("email")));
            txtPhone.setText("Phone: " + cursor.getString(cursor.getColumnIndexOrThrow("phone")));
            txtHouseNo.setText("House No: " + cursor.getString(cursor.getColumnIndexOrThrow("house_no")));
            txtDistrict.setText("District: " + cursor.getString(cursor.getColumnIndexOrThrow("district")));
//            txtRegDate.setText("Registered: " + cursor.getString(cursor.getColumnIndexOrThrow("registration_date")));
        }
        cursor.close();
    }

    private void loadSchedule() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Schedule WHERE customer_id = ?", new String[]{String.valueOf(customerId)});
        if (cursor.moveToFirst()) {
            String next = cursor.getString(cursor.getColumnIndexOrThrow("next_collection"));
            txtNextCollection.setText(next);
            txtCurrCollection.setText("Auto-calculate based on schedule");
            txtPrevCollection.setText("Auto-calculate based on schedule");
        }
        cursor.close();
    }

    private void loadLastExtraPickup() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ExtraPickup WHERE customer_id = ? ORDER BY request_date DESC LIMIT 1", new String[]{String.valueOf(customerId)});
        if (cursor.moveToFirst()) {
            String date = cursor.getString(cursor.getColumnIndexOrThrow("request_date"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
            txtLastExtraPickup.setText(description + " [ on " + date +  "] - (" + status + ")");
        } else {
            txtLastExtraPickup.setText("None");
        }
        cursor.close();
    }

    private void loadLastPayment() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Payment WHERE customer_id = ? ORDER BY payment_date DESC LIMIT 1", new String[]{String.valueOf(customerId)});
        if (cursor.moveToFirst()) {
            String date = cursor.getString(cursor.getColumnIndexOrThrow("payment_date"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
//            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            txtLastPaymentStatus.setText("Paid on [" + date + "] for "  + " (" + status + ")");
        } else {
            txtLastPaymentStatus.setText("No Payment Found");
        }
        cursor.close();
    }

    private void showEditProfileDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_customer, null);
        EditText etName = dialogView.findViewById(R.id.editFullName);
        EditText etPhone = dialogView.findViewById(R.id.editPhone);
        EditText etHouse = dialogView.findViewById(R.id.editHouseNo);
        EditText etDistrict = dialogView.findViewById(R.id.editDistrict);

        CheckBox chkChangePassword = dialogView.findViewById(R.id.chkChangePassword);
        LinearLayout passwordSection = dialogView.findViewById(R.id.passwordSection);

        EditText etCurrentPassword = dialogView.findViewById(R.id.editCurrentPassword);
        EditText etNewPassword = dialogView.findViewById(R.id.editNewPassword);
        EditText etConfirmPassword = dialogView.findViewById(R.id.editConfirmPassword);

        // Hide password section initially
        passwordSection.setVisibility(View.GONE);
        chkChangePassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            passwordSection.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        // Load existing info
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Customer WHERE id = ?", new String[]{String.valueOf(customerId)});
        String currentDbPassword = "";
        if (cursor.moveToFirst()) {
            etName.setText(cursor.getString(cursor.getColumnIndexOrThrow("full_name")));
            etPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
            etHouse.setText(cursor.getString(cursor.getColumnIndexOrThrow("house_no")));
            etDistrict.setText(cursor.getString(cursor.getColumnIndexOrThrow("district")));
            currentDbPassword = cursor.getString(cursor.getColumnIndexOrThrow("password"));
        }
        cursor.close();

        String finalCurrentDbPassword = currentDbPassword;

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Edit Profile")
                .setView(dialogView)
                .setPositiveButton("Save", null) // set listener later
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            Button btnSave = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnSave.setOnClickListener(view -> {
                String name = etName.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String house = etHouse.getText().toString().trim();
                String district = etDistrict.getText().toString().trim();

                if (name.isEmpty() || phone.isEmpty() || house.isEmpty() || district.isEmpty()) {
                    Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (chkChangePassword.isChecked()) {
                    String current = etCurrentPassword.getText().toString();
                    String newPass = etNewPassword.getText().toString();
                    String confirm = etConfirmPassword.getText().toString();

                    if (!current.equals(finalCurrentDbPassword)) {
                        Toast.makeText(this, "Current password incorrect", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!newPass.equals(confirm)) {
                        Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (newPass.length() < 4) {
                        Toast.makeText(this, "Password too short", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    dbHelper.getWritableDatabase().execSQL("UPDATE Customer SET full_name=?, phone=?, house_no=?, district=?, password=? WHERE id=?",
                            new Object[]{name, phone, house, district, newPass, customerId});
                } else {
                    dbHelper.getWritableDatabase().execSQL("UPDATE Customer SET full_name=?, phone=?, house_no=?, district=? WHERE id=?",
                            new Object[]{name, phone, house, district, customerId});
                }

                loadCustomerInfo();
                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        });

        dialog.show();
    }



    private void showExtraPickupsDialog() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ExtraPickup WHERE customer_id = ? ORDER BY request_date DESC", new String[]{String.valueOf(customerId)});

        StringBuilder sb = new StringBuilder();
        int count = 1;
        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndexOrThrow("request_date"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
            String charge = cursor.getString(cursor.getColumnIndexOrThrow("extra_charge"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

            sb.append(count).append(". Date: ").append(date).append("\n")
                    .append("   Status: ").append(status).append("\n")
                    .append("   Charge: TZS ").append(charge).append("\n")
                    .append("   Description: ").append(description).append("\n\n");

            count++;
        }
        cursor.close();

        new AlertDialog.Builder(this)
                .setTitle("Extra Pickup History")
                .setMessage(sb.length() > 0 ? sb.toString() : "No Extra Pickup records found.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showAddExtraPickupDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_extra_pickup, null);
        EditText etDescription = dialogView.findViewById(R.id.etDescription);

        new AlertDialog.Builder(this)
                .setTitle("Request Extra Pickup")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String description = etDescription.getText().toString().trim();

                    if (description.isEmpty()) {
                        Toast.makeText(this, "Description is required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("INSERT INTO ExtraPickup (customer_id, request_date, status, extra_charge, description) " +
                                    "VALUES (?, datetime('now','localtime'), ?, ?, ?)",
                            new Object[]{customerId, "Pending", 2000, description});
                    loadLastExtraPickup();
                    Toast.makeText(this, "Request submitted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void showPaymentsDialog() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Payment WHERE customer_id = ? ORDER BY payment_date DESC", new String[]{String.valueOf(customerId)});

        StringBuilder sb = new StringBuilder();
        int count = 1;
        while (cursor.moveToNext()) {
            String amount = cursor.getString(cursor.getColumnIndexOrThrow("amount"));
            String type = cursor.getString(cursor.getColumnIndexOrThrow("payment_type"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("payment_date"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

            sb.append(count).append(". Date: ").append(date).append("\n")
                    .append("   Amount: TZS ").append(amount).append("\n")
                    .append("   Type: ").append(type).append("\n")
                    .append("   Status: ").append(status).append("\n\n");

            count++;
        }
        cursor.close();

        new AlertDialog.Builder(this)
                .setTitle("Payment History")
                .setMessage(sb.length() > 0 ? sb.toString() : "No payment records found.")
                .setPositiveButton("OK", null)
                .show();
    }


    private void showAddPaymentDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_payment, null);
        EditText etAmount = dialogView.findViewById(R.id.etAmount);
        Spinner spinnerType = dialogView.findViewById(R.id.spinnerType);

        etAmount.setEnabled(false);


        // Setup spinner options
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Monthly", "ExtraPickup"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        // Set default amount when spinner changes
        spinnerType.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selected = spinnerType.getSelectedItem().toString();
                if (selected.equals("Monthly")) {
                    etAmount.setText("5000");
                } else if (selected.equals("ExtraPickup")) {
                    etAmount.setText("2000");
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });

        new AlertDialog.Builder(this)
                .setTitle("Add Payment")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String amount = etAmount.getText().toString().trim();
                    String type = spinnerType.getSelectedItem().toString();
                    String status = "Pending";

                    if (amount.isEmpty()) {
                        Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("INSERT INTO Payment (customer_id, amount, payment_type, payment_date, status) VALUES (?, ?, ?, datetime('now','localtime'), ?)",
                            new Object[]{customerId, amount, type, status});
                    loadLastPayment();
                    Toast.makeText(this, "Payment Added", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


}
