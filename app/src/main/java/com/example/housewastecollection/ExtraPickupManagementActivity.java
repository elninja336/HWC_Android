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

public class ExtraPickupManagementActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btnAddPickup;
    DatabaseHelper dbHelper;
    ArrayList<ExtraPickup> pickupList;
    ExtraPickupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_pickup_management);

        recyclerView = findViewById(R.id.recyclerViewExtraPickup);
        btnAddPickup = findViewById(R.id.btnAddExtraPickup);
        dbHelper = new DatabaseHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadExtraPickups();

        btnAddPickup.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddExtraPickupActivity.class);
            startActivityForResult(intent, 1);
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        loadExtraPickups();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadExtraPickups();
        }
    }


    private void loadExtraPickups() {
        pickupList = dbHelper.getAllExtraPickups();
        HashMap<Integer, String> customerMap = dbHelper.getCustomerNameMap();

        // Prevent null pointer crash
        if (pickupList == null) pickupList = new ArrayList<>();
        if (customerMap == null) customerMap = new HashMap<>();

        if (pickupList.isEmpty()) {
            Toast.makeText(this, "No extra pickups found.", Toast.LENGTH_SHORT).show();
        }
        if (adapter == null) {
            adapter = new ExtraPickupAdapter(this, pickupList, customerMap);
            recyclerView.setAdapter(adapter);
        } else {
            // update existing adapter list and notify it
            adapter.updateData(pickupList, customerMap);
        }

        adapter = new ExtraPickupAdapter(this, pickupList, customerMap);
        recyclerView.setAdapter(adapter);
    }
}
