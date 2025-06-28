package com.example.housewastecollection;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class ManageScheduleActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btnAddSchedule;
    DatabaseHelper dbHelper;
    ArrayList<Schedule> scheduleList;
    ScheduleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_schedule);

        recyclerView = findViewById(R.id.recyclerViewSchedule);
        btnAddSchedule = findViewById(R.id.btnAddSchedule);
        dbHelper = new DatabaseHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadSchedules();

        btnAddSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddScheduleActivity.class);
            startActivityForResult(intent, 1);
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadSchedules();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadSchedules();
        }
    }

    private void loadSchedules() {
        scheduleList = dbHelper.getAllSchedules();
        HashMap<Integer, String> customerMap = dbHelper.getCustomerNameMap();

        if (scheduleList == null) scheduleList = new ArrayList<>();
        if (customerMap == null) customerMap = new HashMap<>();

        if (scheduleList.isEmpty()) {
            Toast.makeText(this, "No schedules found", Toast.LENGTH_SHORT).show();
        }

        adapter = new ScheduleAdapter(this, scheduleList, customerMap);
        recyclerView.setAdapter(adapter);
    }
}
