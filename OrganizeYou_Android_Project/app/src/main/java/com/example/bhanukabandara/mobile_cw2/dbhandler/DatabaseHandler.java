package com.example.bhanukabandara.mobile_cw2.dbhandler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    public static final int DATABASE_VERSION = 85;

    // Database Name
    public static final String DATABASE_NAME = "scheduletime";

    //table names
    public static final String TABLE_APPOINTMENT = "appointment";

    //appointment table columns
    public static final String KEY_APPOINTMENT_ID = "id";
    public static final String KEY_APPOINTMENT_TITLE = "title";
    public static final String KEY_APPOINTMENT_CREATED_DATE = "createdDate";
    public static final String KEY_APPOINTMENT_CREATED_TIME = "createdTime";
    public static final String KEY_APPOINTMENT_DETAILS = "details";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //appointment table create
        String CREATE_CUSTOMER_TABLE = "CREATE TABLE " + TABLE_APPOINTMENT + "("
                + KEY_APPOINTMENT_ID + " TEXT PRIMARY KEY,"
                + KEY_APPOINTMENT_TITLE + " TEXT,"
                + KEY_APPOINTMENT_CREATED_DATE + " TEXT,"
                + KEY_APPOINTMENT_CREATED_TIME + " TEXT,"
                + KEY_APPOINTMENT_DETAILS + " TEXT"
                + ")";
        db.execSQL(CREATE_CUSTOMER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENT);
        onCreate(db);
    }
}
