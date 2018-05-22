package com.example.bhanukabandara.mobile_cw2.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CalendarView;

import com.example.bhanukabandara.mobile_cw2.R;
import com.example.bhanukabandara.mobile_cw2.adapter.AppointmentDisplayAdapter;
import com.example.bhanukabandara.mobile_cw2.dbhandler.AppointmentDbHandler;
import com.example.bhanukabandara.mobile_cw2.model.Appointment;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Appointment> appointmentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AppointmentDisplayAdapter mAdapter;
    FloatingActionMenu floatingActionMenu;
    FloatingActionButton fabSearchAppointment, fabMoveAppointment, fabDeleteAppointment, fabEditAppointment, fabCreateAppointment;
    CalendarView calendarView;

    public String strDay, strMonth, strYear, strDayOfWeek, strDayName;
    public String date;

    AppointmentDbHandler appointmentDbHandler;
    Context context = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);

        appointmentDbHandler = new AppointmentDbHandler(context);
        appointmentDbHandler.open();
        appointmentList = appointmentDbHandler.getAllAppointments();
        appointmentDbHandler.close();
        mAdapter = new AppointmentDisplayAdapter(appointmentList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        floatingActionMenu = findViewById(R.id.floating_menu);
        fabSearchAppointment = findViewById(R.id.fab_search_appointment);
        fabMoveAppointment = findViewById(R.id.fab_move_appointment);
        fabDeleteAppointment = findViewById(R.id.fab_delete_appointment);
        fabEditAppointment = findViewById(R.id.fab_edit_appointment);
        fabCreateAppointment = findViewById(R.id.fab_create_appointment);

        fabCreateAppointment.setOnClickListener(clickListener);
        fabSearchAppointment.setOnClickListener(clickListener);
        fabMoveAppointment.setOnClickListener(clickListener);
        fabDeleteAppointment.setOnClickListener(clickListener);
        fabEditAppointment.setOnClickListener(clickListener);

        calendarView = findViewById(R.id.calendar_view);

        prepareAppointmentData();

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

    }

    /**
     * Method may help to show data to in the list view
     */
    private void prepareAppointmentData() {
        appointmentDbHandler.open();
        appointmentList = appointmentDbHandler.getAllAppointments();
        appointmentDbHandler.close();
        mAdapter.notifyDataSetChanged();

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab_search_appointment:
                    Intent searchIntent = new Intent(MainActivity.this, SearchAppointmentActivity.class);
                    searchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(searchIntent);
                    break;
                case R.id.fab_move_appointment:
                    Intent moveIntent = new Intent(MainActivity.this, MoveAppointmentActivity.class);
                    moveIntent.putExtra("createdDate",date);
                    moveIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(moveIntent);
                    break;
                case R.id.fab_delete_appointment:
                    Intent deleteIntent = new Intent(MainActivity.this, DeleteAppointmentActivity.class);
                    deleteIntent.putExtra("createdDate",date);
                    deleteIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(deleteIntent);
                    break;
                case R.id.fab_edit_appointment:
                    Intent editIntent = new Intent(MainActivity.this, EditAppointmentActivity.class);
                    editIntent.putExtra("createdDate", date);
                    editIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(editIntent);
                    break;
                case R.id.fab_create_appointment:
                    if (date == null) {
                        String currentDate = checkDatePicked();
                        Intent createIntent = new Intent(MainActivity.this, CreateAppointmentActivity.class);
                        createIntent.putExtra("createdDate", currentDate);
                        createIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(createIntent);
                    } else {
                        Intent createIntent = new Intent(MainActivity.this, CreateAppointmentActivity.class);
                        createIntent.putExtra("createdDate", date);
                        createIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(createIntent);
                    }

                    break;
            }
        }
    };

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
}
