package com.example.housewastecollection;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    LinearLayout cardManageCustomer, cardManageSchedule, cardManagePayment, cardManageExtraPickup, cardManageEmployee;
    Button btnLogout;

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
        btnLogout = findViewById(R.id.btnLogout);


        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(AdminDashboardActivity.this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Optional: Clear session or data

                        // Go to login activity
                        Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
                        startActivity(intent);
                        finish(); // Close dashboard
                    })
                    .setNegativeButton("No", null)
                    .show();
        });




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
