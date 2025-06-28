package com.example.housewastecollection;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    LinearLayout cardManageCustomer, cardManageSchedule, cardManagePayment, cardManageExtraPickup, cardManageEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize views
        cardManageCustomer = findViewById(R.id.cardManageCustomer);
        cardManageSchedule = findViewById(R.id.cardManageSchedule);
        cardManagePayment = findViewById(R.id.cardManagePayment);
        cardManageExtraPickup = findViewById(R.id.cardManageExtraPickup);
        cardManageEmployee = findViewById(R.id.cardManageEmployee);

//         Set click listeners using lambda
        cardManageCustomer.setOnClickListener(v ->
                startActivity(new Intent(AdminDashboardActivity.this, CustomerManagementActivity.class))
        );

        cardManageSchedule.setOnClickListener(v ->
                startActivity(new Intent(AdminDashboardActivity.this, ManageScheduleActivity.class))
        );

        cardManagePayment.setOnClickListener(v ->
                startActivity(new Intent(AdminDashboardActivity.this, PaymentManagementActivity.class))
        );

        cardManageExtraPickup.setOnClickListener(v ->
                startActivity(new Intent(AdminDashboardActivity.this, ExtraPickupManagementActivity.class))
        );

        cardManageEmployee.setOnClickListener(v ->
                startActivity(new Intent(AdminDashboardActivity.this, EmployeeManagementActivity.class))
        );
    }
}
