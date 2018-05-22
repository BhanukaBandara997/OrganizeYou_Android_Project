package com.example.bhanukabandara.mobile_cw2.dbhandler;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.example.bhanukabandara.mobile_cw2.model.Appointment;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDbHandler {
    SQLiteDatabase db;
    private Context context;
    private DatabaseHandler dbHelper;

    public AppointmentDbHandler(Context context) {
        this.context = context;
    }

    public AppointmentDbHandler open() throws SQLException {
        dbHelper = new DatabaseHandler(context);
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    // Adding new appointment
    public boolean addAppointment(Appointment appointment) {

        ContentValues values = new ContentValues();
        values.put(dbHelper.KEY_APPOINTMENT_ID, appointment.getAppointmentId()); // appointment id
        values.put(dbHelper.KEY_APPOINTMENT_TITLE, appointment.getAppointmentTitle()); // appointment title
        values.put(dbHelper.KEY_APPOINTMENT_CREATED_DATE, appointment.getAppointmentCreatedDate()); // appointment created date
        values.put(dbHelper.KEY_APPOINTMENT_CREATED_TIME, appointment.getAppointmentCreatedTime()); // appointment created time
        values.put(dbHelper.KEY_APPOINTMENT_DETAILS, appointment.getAppointmentDetails()); // appointment details

        // Inserting Row
        db = dbHelper.getWritableDatabase();
        long result = db.insert(dbHelper.TABLE_APPOINTMENT, null, values);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean isAppointmentExist(String appointmentId) {

        db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + dbHelper.TABLE_APPOINTMENT + " WHERE " + dbHelper.KEY_APPOINTMENT_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{appointmentId});
        int count = cursor.getCount();
        cursor.close();
        return count > 0 ? true : false;
    }

    // Getting All appointments
    public List<Appointment> getAllAppointments() {

        List<Appointment> appointmentList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_APPOINTMENT + " ORDER BY " + dbHelper.KEY_APPOINTMENT_CREATED_DATE + " ASC ";
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Appointment appointment = new Appointment();
                appointment.setAppointmentId(c.getString(c.getColumnIndex(dbHelper.KEY_APPOINTMENT_ID)));
                appointment.setAppointmentTitle(c.getString(c.getColumnIndex(dbHelper.KEY_APPOINTMENT_TITLE)));
                appointment.setAppointmentCreatedDate(c.getString(c.getColumnIndex(dbHelper.KEY_APPOINTMENT_CREATED_DATE)));
                appointment.setAppointmentCreatedTime(c.getString(c.getColumnIndex(dbHelper.KEY_APPOINTMENT_CREATED_TIME)));
                appointment.setAppointmentDetails(c.getString(c.getColumnIndex(dbHelper.KEY_APPOINTMENT_DETAILS)));

                appointmentList.add(appointment);
            } while (c.moveToNext());
        }

        c.close();
        return appointmentList;
    }

    /**
     * Returns all the data for selected date from database
     *
     * @return
     */
    public List<Appointment> getAllAppointmentsForSelectedDate(String selectedDate) {

        List<Appointment> appointmentList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_APPOINTMENT + " WHERE " + dbHelper.KEY_APPOINTMENT_CREATED_DATE + " = ?" + " ORDER BY " + dbHelper.KEY_APPOINTMENT_CREATED_TIME + " ASC ";
        Cursor c = db.rawQuery(selectQuery, new String[]{selectedDate});

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Appointment appointment = new Appointment();
                appointment.setAppointmentId(c.getString(c.getColumnIndex(dbHelper.KEY_APPOINTMENT_ID)));
                appointment.setAppointmentTitle(c.getString(c.getColumnIndex(dbHelper.KEY_APPOINTMENT_TITLE)));
                appointment.setAppointmentCreatedDate(c.getString(c.getColumnIndex(dbHelper.KEY_APPOINTMENT_CREATED_DATE)));
                appointment.setAppointmentCreatedTime(c.getString(c.getColumnIndex(dbHelper.KEY_APPOINTMENT_CREATED_TIME)));
                appointment.setAppointmentDetails(c.getString(c.getColumnIndex(dbHelper.KEY_APPOINTMENT_DETAILS)));

                appointmentList.add(appointment);
            } while (c.moveToNext());
        }

        c.close();
        return appointmentList;
    }

    /**
     * Returns all the data from database
     *
     * @return
     */
    public int getAppointmentCount() {
        db = dbHelper.getReadableDatabase();
        String countQuery = "SELECT  * FROM " + dbHelper.TABLE_APPOINTMENT;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    /**
     * Delete appointment data from database
     *
     * @return
     */
    public boolean deleteAppointment(String appointmentId) {
        boolean success = false;

        if (isAppointmentExist(appointmentId)) {
            db = dbHelper.getWritableDatabase();
            db.delete(dbHelper.TABLE_APPOINTMENT, dbHelper.KEY_APPOINTMENT_ID + "=?", new String[]{String.valueOf(appointmentId)});
            success = !isAppointmentExist(appointmentId);
        } else {
            success = true;
        }
        return success;
    }

    /**
     * Delete appointment data from database
     *
     * @return
     */
    public boolean deleteAppointmentForSelectedDate(String createdDate) {
        db = dbHelper.getWritableDatabase();
        int result = db.delete(dbHelper.TABLE_APPOINTMENT, dbHelper.KEY_APPOINTMENT_CREATED_DATE + "=?", new String[]{String.valueOf(createdDate)});

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Updates a Appointment record to the database.
     * Performs an insert or update operation on the appointment table.
     *
     * @param appointmentId
     * @param appointmentTitle
     * @param appointmentCreatedDate
     * @param appointmentCreatedTime
     * @param appointmentDetails
     */
    public boolean updateAppointment(String appointmentId,String appointmentTitle, String appointmentCreatedDate, String appointmentCreatedTime, String appointmentDetails) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.KEY_APPOINTMENT_ID, appointmentId);
        contentValues.put(dbHelper.KEY_APPOINTMENT_TITLE, appointmentTitle);
        contentValues.put(dbHelper.KEY_APPOINTMENT_CREATED_DATE, appointmentCreatedDate);
        contentValues.put(dbHelper.KEY_APPOINTMENT_CREATED_TIME, appointmentCreatedTime);
        contentValues.put(dbHelper.KEY_APPOINTMENT_DETAILS, appointmentDetails);
        int i =  db.update(dbHelper.TABLE_APPOINTMENT, contentValues, dbHelper.KEY_APPOINTMENT_ID + "=?", new String[] {appointmentId});
        return i > 0;
    }

    /**
     * Updates a Appointment created date to the database.
     * Performs an insert or update operation on the appointment table.
     *
     * @param appointmentId
     * @param newAppointmentDate
     */
    public boolean updateAppointmentCreatedDate(String appointmentId,String newAppointmentDate) {
        ContentValues cv = new ContentValues();
        cv.put(dbHelper.KEY_APPOINTMENT_CREATED_DATE, newAppointmentDate);
        int result = db.update(dbHelper.TABLE_APPOINTMENT, cv, dbHelper.KEY_APPOINTMENT_ID + "=?", new String[] {appointmentId});
        return result > 0;
    }
}
