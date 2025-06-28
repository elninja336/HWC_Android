package com.example.housewastecollection;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "HouseWasteCollection.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_CUSTOMER = "Customer";
    public static final String TABLE_SCHEDULE = "Schedule";
    public static final String TABLE_EXTRA_PICKUP = "ExtraPickup";
    public static final String TABLE_PAYMENT = "Payment";
    public static final String TABLE_EMPLOYEE = "Employee";

    public static final String TABLE_LOGIN = "Login";

    // Customer Table Columns
    public static final String CUSTOMER_ID = "id";
    public static final String CUSTOMER_FULL_NAME = "full_name";
    public static final String CUSTOMER_EMAIL = "email";
    public static final String CUSTOMER_PHONE = "phone";
    public static final String CUSTOMER_HOUSE_NO = "house_no";
    public static final String CUSTOMER_DISTRICT = "district";
    public static final String CUSTOMER_PASSWORD = "password";
    public static final String CUSTOMER_REGISTRATION_DATE = "registration_date";

    // Schedule Table Columns
    public static final String SCHEDULE_ID = "id";
    public static final String SCHEDULE_CUSTOMER_ID = "customer_id";
    public static final String SCHEDULE_DAY_OF_WEEK = "day_of_week";
    public static final String SCHEDULE_NEXT_COLLECTION = "next_collection";

    // ExtraPickup Table Columns
    public static final String EXTRAPICKUP_ID = "id";
    public static final String EXTRAPICKUP_CUSTOMER_ID = "customer_id";
    public static final String EXTRAPICKUP_REQUEST_DATE = "request_date";
    public static final String EXTRAPICKUP_STATUS = "status";
    public static final String EXTRAPICKUP_DESCRIPTION = "description";

    public static final String EXTRAPICKUP_EXTRA_CHARGE = "extra_charge";


    // Payment Table Columns
    public static final String PAYMENT_ID = "id";
    public static final String PAYMENT_CUSTOMER_ID = "customer_id";
    public static final String PAYMENT_AMOUNT = "amount";
    public static final String PAYMENT_TYPE = "payment_type";
    public static final String PAYMENT_DATE = "payment_date";
    public static final String PAYMENT_STATUS = "status";

    // Employee Table Columns
    public static final String EMPLOYEE_ID = "id";
    public static final String EMPLOYEE_FULL_NAME = "full_name";
    public static final String EMPLOYEE_EMAIL = "email";
    public static final String EMPLOYEE_PHONE = "phone";
    public static final String EMPLOYEE_ASSIGNED_AREA = "assigned_area";
    public static final String EMPLOYEE_HIRE_DATE = "hire_date";

    // Login table columns

    public static final String  LOGIN_ID = "id";

    public static final String  LOGIN_USERNAME = "username";

    public static final String  LOGIN_PASSWORD= "password";

    public static final String  LOGIN_ROLE = "role";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Customer Table
        String CREATE_CUSTOMER_TABLE = "CREATE TABLE " + TABLE_CUSTOMER + "("
                + CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CUSTOMER_FULL_NAME + " TEXT, "
                + CUSTOMER_EMAIL + " TEXT, "
                + CUSTOMER_PHONE + " TEXT, "
                + CUSTOMER_HOUSE_NO + " TEXT, "
                + CUSTOMER_DISTRICT + " TEXT, "
                + CUSTOMER_PASSWORD + " TEXT DEFAULT '1234', "
                + CUSTOMER_REGISTRATION_DATE + " TEXT"
                + ")";
        db.execSQL(CREATE_CUSTOMER_TABLE);

        // Create Schedule Table
        String CREATE_SCHEDULE_TABLE = "CREATE TABLE " + TABLE_SCHEDULE + "("
                + SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SCHEDULE_CUSTOMER_ID + " INTEGER, "
                + SCHEDULE_DAY_OF_WEEK + " TEXT, "
                + SCHEDULE_NEXT_COLLECTION + " TEXT, "
                + "FOREIGN KEY(" + SCHEDULE_CUSTOMER_ID + ") REFERENCES " + TABLE_CUSTOMER + "(" + CUSTOMER_ID + ")"
                + ")";
        db.execSQL(CREATE_SCHEDULE_TABLE);

        // Create ExtraPickup Table
        String CREATE_EXTRAPICKUP_TABLE = "CREATE TABLE " + TABLE_EXTRA_PICKUP + "("
                + EXTRAPICKUP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EXTRAPICKUP_CUSTOMER_ID + " INTEGER, "
                + EXTRAPICKUP_REQUEST_DATE + " TEXT, "
                + EXTRAPICKUP_STATUS + " TEXT, "
                + EXTRAPICKUP_DESCRIPTION + " TEXT, "
                + EXTRAPICKUP_EXTRA_CHARGE + " REAL, "
                + "FOREIGN KEY(" + EXTRAPICKUP_CUSTOMER_ID + ") REFERENCES " + TABLE_CUSTOMER + "(" + CUSTOMER_ID + ")"
                + ")";
        db.execSQL(CREATE_EXTRAPICKUP_TABLE);

        // Create Payment Table
        String CREATE_PAYMENT_TABLE = "CREATE TABLE " + TABLE_PAYMENT + "("
                + PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PAYMENT_CUSTOMER_ID + " INTEGER, "
                + PAYMENT_AMOUNT + " REAL, "
                + PAYMENT_TYPE + " TEXT, "
                + PAYMENT_DATE + " TEXT DEFAULT (datetime('now','localtime')), "
                + PAYMENT_STATUS + " TEXT, "
                + "FOREIGN KEY(" + PAYMENT_CUSTOMER_ID + ") REFERENCES " + TABLE_CUSTOMER + "(" + CUSTOMER_ID + ")"
                + ")";
        db.execSQL(CREATE_PAYMENT_TABLE);



        // Create Employee Table
        String CREATE_EMPLOYEE_TABLE = "CREATE TABLE " + TABLE_EMPLOYEE + "("
                + EMPLOYEE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EMPLOYEE_FULL_NAME + " TEXT, "
                + EMPLOYEE_EMAIL + " TEXT, "
                + EMPLOYEE_PHONE + " TEXT, "
                + EMPLOYEE_ASSIGNED_AREA + " TEXT, "
                + EMPLOYEE_HIRE_DATE + " TEXT"
                + ")";
        db.execSQL(CREATE_EMPLOYEE_TABLE);

        // Create Login Table
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + LOGIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LOGIN_USERNAME + " TEXT, "
                + LOGIN_PASSWORD + " TEXT, "
                + LOGIN_ROLE + " TEXT "
                + ")";
        db.execSQL(CREATE_LOGIN_TABLE);


        String insertAdmin = "INSERT INTO Login (username, password, role) VALUES ('admin', '1234', 'admin')";
        String insertStaff = "INSERT INTO Login (username, password, role) VALUES ('staff', 'staff', 'staff')";
        String insertUser = "INSERT INTO Login (username, password, role) VALUES ('user', '1234', 'user')";
        db.execSQL(insertAdmin);
        db.execSQL(insertStaff);
        db.execSQL(insertUser);




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old tables if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXTRA_PICKUP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);


        // Create tables again
        onCreate(db);
    }

    public String getCustomerNameById(int customerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String name = "Unknown";
        Cursor cursor = db.rawQuery("SELECT " + CUSTOMER_FULL_NAME +
                " FROM " + TABLE_CUSTOMER + " WHERE " + CUSTOMER_ID + " = ?", new String[]{String.valueOf(customerId)});
        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }
        cursor.close();
        return name;
    }
    public ArrayList<ExtraPickup> getAllExtraPickups() {
        ArrayList<ExtraPickup> pickupList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXTRA_PICKUP, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(EXTRAPICKUP_ID));
                int customerId = cursor.getInt(cursor.getColumnIndexOrThrow(EXTRAPICKUP_CUSTOMER_ID));
                String requestDate = cursor.getString(cursor.getColumnIndexOrThrow(EXTRAPICKUP_REQUEST_DATE));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(EXTRAPICKUP_STATUS));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(EXTRAPICKUP_DESCRIPTION));
                double charge = cursor.getDouble(cursor.getColumnIndexOrThrow(EXTRAPICKUP_EXTRA_CHARGE));


                pickupList.add(new ExtraPickup(id, customerId, requestDate, status, description, charge));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return pickupList;
    }
    public HashMap<Integer, String> getCustomerNameMap() {
        HashMap<Integer, String> customerMap = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, full_name FROM " + TABLE_CUSTOMER, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                customerMap.put(id, name);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return customerMap;
    }
    // Fetch all schedules from DB
    public ArrayList<Schedule> getAllSchedules() {
        ArrayList<Schedule> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SCHEDULE, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int customerId = cursor.getInt(cursor.getColumnIndexOrThrow(SCHEDULE_CUSTOMER_ID));
                String dayOfWeek = cursor.getString(cursor.getColumnIndexOrThrow(SCHEDULE_DAY_OF_WEEK));
                String nextCollection = cursor.getString(cursor.getColumnIndexOrThrow(SCHEDULE_NEXT_COLLECTION));

                list.add(new Schedule(id, customerId, dayOfWeek, nextCollection));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Delete a schedule by id
    public int deleteSchedule(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SCHEDULE, "id=?", new String[]{String.valueOf(id)});
    }

    public boolean checkCustomerLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CUSTOMER +
                        " WHERE " + CUSTOMER_EMAIL + " = ? AND " + CUSTOMER_PASSWORD + " = ?",
                new String[]{email, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    public int getCustomerIdByEmailAndPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_CUSTOMER + " WHERE email = ? AND password = ?", new String[]{email, password});
        int customerId = -1;
        if (cursor.moveToFirst()) {
            customerId = cursor.getInt(0);
        }
        cursor.close();
        return customerId;
    }

}

