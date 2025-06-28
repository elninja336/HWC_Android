package com.example.housewastecollection;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.*;

public class AddExtraPickupActivity extends AppCompatActivity {

    Spinner spinnerCustomer, spinnerStatus;
    EditText etExtraCharge, etDescription;
    Button btnSave, btnCancel;

    DatabaseHelper dbHelper;
    ArrayList<String> customerNames;
    HashMap<String, Integer> customerMap;

    int editingId = -1; // used for editing an existing pickup

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_extra_pickup);

        dbHelper = new DatabaseHelper(this);
        spinnerCustomer = findViewById(R.id.spinnerCustomer);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        etExtraCharge = findViewById(R.id.etExtraCharge);
        etDescription = findViewById(R.id.etDescription);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        setupStatusSpinner();
        loadCustomers();

        // Check if editing
        editingId = getIntent().getIntExtra("extra_pickup_id", -1);

        if (editingId != -1) {
            loadPickupData();
        }

        btnSave.setOnClickListener(v -> saveExtraPickup());

        btnCancel.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Cancel")
                    .setMessage("Are you sure you want to cancel?")
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void setupStatusSpinner() {
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Pending", "Completed"});
        spinnerStatus.setAdapter(statusAdapter);
    }

    private void loadCustomers() {
        customerNames = new ArrayList<>();
        customerMap = new HashMap<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, full_name FROM " + DatabaseHelper.TABLE_CUSTOMER, null);

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

    private void loadPickupData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_EXTRA_PICKUP + " WHERE id = ?", new String[]{String.valueOf(editingId)});
        if (cursor.moveToFirst()) {
            int customerId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.EXTRAPICKUP_CUSTOMER_ID));
            double charge = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.EXTRAPICKUP_EXTRA_CHARGE));
            String status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.EXTRAPICKUP_STATUS));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.EXTRAPICKUP_DESCRIPTION));
            etDescription.setText(description);


            etExtraCharge.setText(String.valueOf(charge));
            spinnerStatus.setSelection(status.equals("Pending") ? 0 : 1);

            for (int i = 0; i < customerNames.size(); i++) {
                if (customerMap.get(customerNames.get(i)) == customerId) {
                    spinnerCustomer.setSelection(i);
                    break;
                }
            }
        }
        cursor.close();
    }

    private void saveExtraPickup() {
        String selectedCustomer = spinnerCustomer.getSelectedItem().toString();
        int customerId = customerMap.get(selectedCustomer);
        String status = spinnerStatus.getSelectedItem().toString();
        String chargeStr = etExtraCharge.getText().toString().trim();
        String description = etDescription.getText().toString().trim();


        if (chargeStr.isEmpty()) {
            Toast.makeText(this, "Please enter extra charge", Toast.LENGTH_SHORT).show();
            return;
        }

        double extraCharge = Double.parseDouble(chargeStr);

        // Get today's date
        String requestDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.EXTRAPICKUP_CUSTOMER_ID, customerId);
        values.put(DatabaseHelper.EXTRAPICKUP_EXTRA_CHARGE, extraCharge);
        values.put(DatabaseHelper.EXTRAPICKUP_STATUS, status);
        values.put(DatabaseHelper.EXTRAPICKUP_REQUEST_DATE, requestDate);
        values.put(DatabaseHelper.EXTRAPICKUP_DESCRIPTION, description);


        long result;
        if (editingId == -1) {
            result = db.insert(DatabaseHelper.TABLE_EXTRA_PICKUP, null, values);
        } else {
            result = db.update(DatabaseHelper.TABLE_EXTRA_PICKUP, values, "id=?", new String[]{String.valueOf(editingId)});
        }

        if (result != -1) {
            Toast.makeText(this, "Extra pickup saved", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        }
        else {
            Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show();
        }

    }
}
