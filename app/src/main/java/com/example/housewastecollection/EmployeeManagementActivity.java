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

public class EmployeeManagementActivity extends AppCompatActivity {

    Button btnAddEmployee;
    RecyclerView recyclerView;
    EmployeeAdapter adapter;
    DatabaseHelper dbHelper;
    List<Employee> employeeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_management); // Make sure this XML matches structure like customer one

        btnAddEmployee = findViewById(R.id.btnAddEmployee);
        recyclerView = findViewById(R.id.recyclerViewEmployees);
        dbHelper = new DatabaseHelper(this);
        employeeList = new ArrayList<>();

        // Updated: Adapter with Edit/Delete interface
        adapter = new EmployeeAdapter(this, employeeList, new EmployeeAdapter.OnEmployeeActionListener() {
            @Override
            public void onEdit(Employee employee) {
                // Example: Launch AddEmployeeActivity with employee ID (you can modify to pass data)
                Intent intent = new Intent(EmployeeManagementActivity.this, AddEmployeeActivity.class);
                intent.putExtra("employee_id", employee.getId());
                startActivity(intent);
            }

            @Override
            public void onDelete(Employee employee) {
                new AlertDialog.Builder(EmployeeManagementActivity.this)
                        .setTitle("Delete Employee")
                        .setMessage("Are you sure you want to delete " + employee.getFullName() + "?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            deleteEmployee(employee.getId());
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnAddEmployee.setOnClickListener(v -> {
            startActivity(new Intent(EmployeeManagementActivity.this, AddEmployeeActivity.class));
        });

        loadEmployees(); // Initial load
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEmployees(); // Refresh when returning from add/edit screen
    }

    private void loadEmployees() {
        employeeList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_EMPLOYEE, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.EMPLOYEE_ID));
                String fullName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.EMPLOYEE_FULL_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.EMPLOYEE_EMAIL));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.EMPLOYEE_PHONE));
                String area = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.EMPLOYEE_ASSIGNED_AREA));
                String hireDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.EMPLOYEE_HIRE_DATE));

                employeeList.add(new Employee(id, fullName, email, phone, area, hireDate));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        adapter.updateList(employeeList); // Update adapter with new data
    }

    private void deleteEmployee(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deleted = db.delete(DatabaseHelper.TABLE_EMPLOYEE, DatabaseHelper.EMPLOYEE_ID + "=?", new String[]{String.valueOf(id)});
        if (deleted > 0) {
            Toast.makeText(this, "Employee deleted", Toast.LENGTH_SHORT).show();
            loadEmployees();
        } else {
            Toast.makeText(this, "Failed to delete employee", Toast.LENGTH_SHORT).show();
        }
    }
}
