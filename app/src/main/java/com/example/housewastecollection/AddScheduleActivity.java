package com.example.housewastecollection;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.*;

public class AddScheduleActivity extends AppCompatActivity {

    Spinner spinnerCustomer, spinnerDayOfWeek;
    EditText etNextCollection;
    Button btnSave, btnCancel;

    DatabaseHelper dbHelper;
    ArrayList<String> customerNames;
    HashMap<String, Integer> customerMap;

    int editingId = -1; // for editing existing schedule

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        dbHelper = new DatabaseHelper(this);
        spinnerCustomer = findViewById(R.id.spinnerCustomer);
        spinnerDayOfWeek = findViewById(R.id.spinnerDayOfWeek);
        etNextCollection = findViewById(R.id.etNextCollection);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        loadCustomers();
        setupDayOfWeekSpinner();

        editingId = getIntent().getIntExtra("schedule_id", -1);
        if (editingId != -1) {
            loadScheduleData();
        }

        btnSave.setOnClickListener(v -> saveSchedule());

        btnCancel.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Cancel")
                .setMessage("Are you sure you want to cancel?")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", null)
                .show());
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

    private void setupDayOfWeekSpinner() {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, days);
        spinnerDayOfWeek.setAdapter(adapter);
    }

    private void loadScheduleData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_SCHEDULE + " WHERE id = ?", new String[]{String.valueOf(editingId)});
        if (cursor.moveToFirst()) {
            int customerId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.SCHEDULE_CUSTOMER_ID));
            String dayOfWeek = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.SCHEDULE_DAY_OF_WEEK));
            String nextCollection = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.SCHEDULE_NEXT_COLLECTION));

            etNextCollection.setText(nextCollection);

            for (int i = 0; i < customerNames.size(); i++) {
                if (customerMap.get(customerNames.get(i)) == customerId) {
                    spinnerCustomer.setSelection(i);
                    break;
                }
            }

            for (int i = 0; i < spinnerDayOfWeek.getCount(); i++) {
                if (spinnerDayOfWeek.getItemAtPosition(i).toString().equalsIgnoreCase(dayOfWeek)) {
                    spinnerDayOfWeek.setSelection(i);
                    break;
                }
            }
        }
        cursor.close();
    }

    private void saveSchedule() {
        String selectedCustomer = spinnerCustomer.getSelectedItem().toString();
        int customerId = customerMap.get(selectedCustomer);
        String dayOfWeek = spinnerDayOfWeek.getSelectedItem().toString();
        String nextCollection = etNextCollection.getText().toString().trim();

        if (nextCollection.isEmpty()) {
            Toast.makeText(this, "Please enter next collection date", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.SCHEDULE_CUSTOMER_ID, customerId);
        values.put(DatabaseHelper.SCHEDULE_DAY_OF_WEEK, dayOfWeek);
        values.put(DatabaseHelper.SCHEDULE_NEXT_COLLECTION, nextCollection);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long result;
        if (editingId == -1) {
            result = db.insert(DatabaseHelper.TABLE_SCHEDULE, null, values);
        } else {
            result = db.update(DatabaseHelper.TABLE_SCHEDULE, values, "id=?", new String[]{String.valueOf(editingId)});
        }

        if (result != -1) {
            Toast.makeText(this, "Schedule saved", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show();
        }
    }
}
