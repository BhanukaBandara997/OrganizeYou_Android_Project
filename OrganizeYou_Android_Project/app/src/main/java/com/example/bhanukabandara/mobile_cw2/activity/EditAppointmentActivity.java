package com.example.bhanukabandara.mobile_cw2.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.example.bhanukabandara.mobile_cw2.R;
import com.example.bhanukabandara.mobile_cw2.adapter.AppointmentDeleteAdapter;
import com.example.bhanukabandara.mobile_cw2.adapter.AppointmentEditAdapter;
import com.example.bhanukabandara.mobile_cw2.dbhandler.AppointmentDbHandler;
import com.example.bhanukabandara.mobile_cw2.model.Appointment;
import java.util.List;

public class EditAppointmentActivity extends AppCompatActivity implements RecyclerEditItemTouchHelper.RecyclerItemTouchHelperListener {

    private RecyclerView recyclerView;
    private List<Appointment> appointmentList;
    private AppointmentEditAdapter mAdapter;
    CoordinatorLayout coordinatorLayout;
    AppointmentDbHandler appointmentDbHandler;
    Context context = EditAppointmentActivity.this;
    String date;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.edit_list));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();
        date = intent.getStringExtra("createdDate");

        recyclerView = findViewById(R.id.recycler_view);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        appointmentDbHandler = new AppointmentDbHandler(context);
        appointmentDbHandler.open();
        appointmentList = appointmentDbHandler.getAllAppointmentsForSelectedDate(date);
        appointmentDbHandler.close();
        mAdapter = new AppointmentEditAdapter(appointmentList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerEditItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        // making http call and fetching menu json
        prepareAppointmentData();

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);
    }

    /**
     * method may help to show data to in the list view
     */
    private void prepareAppointmentData() {
        appointmentDbHandler.open();
        appointmentList = appointmentDbHandler.getAllAppointmentsForSelectedDate(date);
        appointmentDbHandler.close();
        mAdapter.notifyDataSetChanged();

    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof AppointmentEditAdapter.EditViewHolder) {
            appointmentList.get(viewHolder.getAdapterPosition()).getAppointmentTitle();

            final Appointment editedAppointment = appointmentList.get(viewHolder.getAdapterPosition());
            String appointmentId = editedAppointment.getAppointmentId();
            String appointmentTitle = editedAppointment.getAppointmentTitle();
            String appointmentCreatedDate = editedAppointment.getAppointmentCreatedDate();
            String appointmentCreatedTime = editedAppointment.getAppointmentCreatedTime();
            String appointmentDetails = editedAppointment.getAppointmentDetails();

            Intent intent = new Intent(EditAppointmentActivity.this, EditItemActivity.class);
            intent.putExtra("editAppointmentId",appointmentId);
            intent.putExtra("editAppointmentTitle",appointmentTitle);
            intent.putExtra("editAppointmentCreatedDate",appointmentCreatedDate);
            intent.putExtra("editAppointmentCreatedTime",appointmentCreatedTime);
            intent.putExtra("editAppointmentDetails",appointmentDetails);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(EditAppointmentActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        Intent intent = new Intent(EditAppointmentActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}
