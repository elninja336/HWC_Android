package com.example.housewastecollection;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText etFullName, etEmail, etPhone, etHouseNo, etDistrict, etPassword, etConfirmPassword;
    Button btnRegister, btnCancel;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etHouseNo = findViewById(R.id.etHouseNo);
        etDistrict = findViewById(R.id.etDistrict);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnCancel = findViewById(R.id.btnCancel);

        dbHelper = new DatabaseHelper(this);

        btnCancel.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Cancel")
                    .setMessage("Are you sure you want to cancel and go back?")
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .setNegativeButton("No", null)
                    .show();
        });

        btnRegister.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String houseNo = etHouseNo.getText().toString().trim();
            String district = etDistrict.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() ||
                    houseNo.isEmpty() || district.isEmpty() ||
                    password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insert into DB
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.CUSTOMER_FULL_NAME, fullName);
            values.put(DatabaseHelper.CUSTOMER_EMAIL, email);
            values.put(DatabaseHelper.CUSTOMER_PHONE, phone);
            values.put(DatabaseHelper.CUSTOMER_HOUSE_NO, houseNo);
            values.put(DatabaseHelper.CUSTOMER_DISTRICT, district);
            values.put(DatabaseHelper.CUSTOMER_PASSWORD, password);
            values.put(DatabaseHelper.CUSTOMER_REGISTRATION_DATE, getCurrentDateTime());

            long result = db.insert(DatabaseHelper.TABLE_CUSTOMER, null, values);
            if (result != -1) {
                Toast.makeText(this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                finish(); // Return to login
            } else {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 👇 Simple method to return current date-time
    private String getCurrentDateTime() {
        long currentTimeMillis = System.currentTimeMillis();
        return android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", currentTimeMillis).toString();
    }
}
