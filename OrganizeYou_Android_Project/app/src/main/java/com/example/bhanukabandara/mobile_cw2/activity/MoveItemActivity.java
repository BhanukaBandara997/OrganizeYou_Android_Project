package com.example.bhanukabandara.mobile_cw2.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;
import com.example.bhanukabandara.mobile_cw2.R;
import com.example.bhanukabandara.mobile_cw2.dbhandler.AppointmentDbHandler;
import com.example.bhanukabandara.mobile_cw2.model.Appointment;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MoveItemActivity extends AppCompatActivity {

    Intent intent;
    String appointmentId, appointmentTitle, appointmentCreatedDate, appointmentCreatedTime, appointmentDetails;
    Button saveAppointmentBtn;
    public static List<Appointment> appointmentList = new ArrayList<>();
    CalendarView calendarView;
    public String strDay, strMonth, strYear, strDayOfWeek, strDayName;
    public String date;
    Context context = MoveItemActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.move));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saveAppointmentBtn = findViewById(R.id.save_button);
        calendarView = findViewById(R.id.calendar_view);

        intent = getIntent();
        appointmentId = intent.getStringExtra("moveAppointmentId");
        appointmentTitle = intent.getStringExtra("moveAppointmentTitle");
        appointmentCreatedDate = intent.getStringExtra("moveAppointmentCreatedDate");
        appointmentCreatedTime = intent.getStringExtra("moveAppointmentCreatedTime");
        appointmentDetails = intent.getStringExtra("moveAppointmentDetails");

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                strDay = String.valueOf(dayOfMonth);
                strMonth = String.valueOf(month);
                strYear = String.valueOf(year);
                strDayOfWeek = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
                switch (strDayOfWeek) {
                    case "1":
                        strDayName = "SUN";
                        break;
                    case "2":
                        strDayName = "MON";
                        break;
                    case "3":
                        strDayName = "TUE";
                        break;
                    case "4":
                        strDayName = "WED";
                        break;
                    case "5":
                        strDayName = "THUR";
                        break;
                    case "6":
                        strDayName = "FRI";
                        break;
                    case "7":
                        strDayName = "SAT";
                        break;
                    default:
                        break;
                }
                date = strDayName + " " + strDay + "-" + strMonth + "-" + strYear;
            }
        });

        checkDatePicked();

        saveAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isExist = false;
                boolean isSaveCompleted = false;
                AppointmentDbHandler dbAdapter = new AppointmentDbHandler(context);
                dbAdapter.open();
                int appointmentCount = dbAdapter.getAppointmentCount();
                Log.i("appointmentCount",String.valueOf(appointmentCount));
                if (date.equals(appointmentCreatedDate)) {
                    isExist = true;
                }
                if (isExist) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);

                    // set title
                    alertDialogBuilder.setTitle("Appointment Already Exist");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Appointment already in that date select a different date to save")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, close the dialog
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                } else if (!isExist) {
                    isSaveCompleted = dbAdapter.updateAppointmentCreatedDate(appointmentId, date);
                }
                dbAdapter.close();
                if (isSaveCompleted) {
                    Toast.makeText(MoveItemActivity.this, "Updated Created Date Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MoveItemActivity.this, MoveAppointmentActivity.class);
                    intent.putExtra("createdDate", date);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (!isSaveCompleted) {
                    Toast.makeText(MoveItemActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public String checkDatePicked() {
        if (date == null) {
            Calendar c = Calendar.getInstance();

            strDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
            strMonth = String.valueOf(c.get(Calendar.MONTH));
            strYear = String.valueOf(c.get(Calendar.YEAR));
            strDayOfWeek = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
            switch (strDayOfWeek) {
                case "1":
                    strDayName = "SUN";
                    break;
                case "2":
                    strDayName = "MON";
                    break;
                case "3":
                    strDayName = "TUE";
                    break;
                case "4":
                    strDayName = "WED";
                    break;
                case "5":
                    strDayName = "THUR";
                    break;
                case "6":
                    strDayName = "FRI";
                    break;
                case "7":
                    strDayName = "SAT";
                    break;
                default:
                    break;
            }
            date = strDayName + " " + strDay + "-" + strMonth + "-" + strYear;
        }
        return date;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(MoveItemActivity.this, MoveAppointmentActivity.class);
            intent.putExtra("createdDate", date);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        Intent intent = new Intent(MoveItemActivity.this, MoveAppointmentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}
