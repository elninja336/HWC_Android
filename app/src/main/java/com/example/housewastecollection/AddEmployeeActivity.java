package com.example.housewastecollection;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddEmployeeActivity extends AppCompatActivity {

    EditText etFullName, etEmail, etPhone, etAssignedArea, etHireDate;
    Button btnSave, btnCancel;

    DatabaseHelper dbHelper;
    int employeeId = -1; // default for new employee

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        dbHelper = new DatabaseHelper(this);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAssignedArea = findViewById(R.id.etAssignedArea);
        etHireDate = findViewById(R.id.etHireDate);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // Check if editing existing employee (id passed from intent)
        if (getIntent().hasExtra("employee_id")) {
            employeeId = getIntent().getIntExtra("employee_id", -1);
            if (employeeId != -1) {
                loadEmployeeDetails(employeeId);
            }
        }

        btnSave.setOnClickListener(v -> saveEmployee());

        btnCancel.setOnClickListener(v -> {
            new AlertDialog.Builder(AddEmployeeActivity.this)
                    .setTitle("Cancel")
                    .setMessage("Are you sure you want to cancel and go back?")
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void loadEmployeeDetails(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_EMPLOYEE, null,
                DatabaseHelper.EMPLOYEE_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            etFullName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.EMPLOYEE_FULL_NAME)));
            etEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.EMPLOYEE_EMAIL)));
            etPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.EMPLOYEE_PHONE)));
            etAssignedArea.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.EMPLOYEE_ASSIGNED_AREA)));
            etHireDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.EMPLOYEE_HIRE_DATE)));
            cursor.close();
        }
    }

    private void saveEmployee() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String assignedArea = etAssignedArea.getText().toString().trim();
        String hireDate = etHireDate.getText().toString().trim();

        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Full name, email, and phone are required", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.EMPLOYEE_FULL_NAME, fullName);
        values.put(DatabaseHelper.EMPLOYEE_EMAIL, email);
        values.put(DatabaseHelper.EMPLOYEE_PHONE, phone);
        values.put(DatabaseHelper.EMPLOYEE_ASSIGNED_AREA, assignedArea);
        values.put(DatabaseHelper.EMPLOYEE_HIRE_DATE, hireDate);

        long result;
        if (employeeId == -1) {
            // Insert new employee
            result = db.insert(DatabaseHelper.TABLE_EMPLOYEE, null, values);
        } else {
            // Update existing employee
            result = db.update(DatabaseHelper.TABLE_EMPLOYEE, values,
                    DatabaseHelper.EMPLOYEE_ID + "=?",
                    new String[]{String.valueOf(employeeId)});
        }

        if (result != -1) {
            Toast.makeText(this, employeeId == -1 ? "Employee added successfully" : "Employee updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to save employee", Toast.LENGTH_SHORT).show();
        }
    }
}
