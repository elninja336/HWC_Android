package com.example.housewastecollection;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin;
    TextView tvGoToRegister;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);

        dbHelper = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getReadableDatabase();

            // ✅ First, check Login table for admin/staff
            Cursor loginCursor = db.rawQuery("SELECT role FROM Login WHERE username = ? AND password = ?", new String[]{username, password});
            if (loginCursor.moveToFirst()) {
                String role = loginCursor.getString(0);
                Toast.makeText(this, "Login successful as " + role, Toast.LENGTH_SHORT).show();

                if (role.equalsIgnoreCase("admin")) {
                    startActivity(new Intent(this, AdminDashboardActivity.class));
                } else if (role.equalsIgnoreCase("staff")) {
                    startActivity(new Intent(this, Schedule.class));
                }

                loginCursor.close();
                finish();
                return;
            }
            loginCursor.close();

            // check Customer table using email and password
            int customerId = dbHelper.getCustomerIdByEmailAndPassword(username, password);
            if (customerId != -1) {
                Toast.makeText(this, "Login successful as Customer", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, CustomerDashboardActivity.class);
                intent.putExtra("customer_id", customerId);
                intent.putExtra("email", username); // Optional
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid login credentials", Toast.LENGTH_SHORT).show();
            }
        });

        tvGoToRegister.setOnClickListener(v -> {
            Toast.makeText(this, "Redirect to Register Page", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}
