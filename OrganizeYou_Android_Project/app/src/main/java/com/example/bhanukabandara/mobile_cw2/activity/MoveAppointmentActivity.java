package com.example.bhanukabandara.mobile_cw2.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import com.example.bhanukabandara.mobile_cw2.R;
import com.example.bhanukabandara.mobile_cw2.adapter.AppointmentEditAdapter;
import com.example.bhanukabandara.mobile_cw2.adapter.AppointmentMoveAdapter;
import com.example.bhanukabandara.mobile_cw2.dbhandler.AppointmentDbHandler;
import com.example.bhanukabandara.mobile_cw2.model.Appointment;

import java.util.List;

public class MoveAppointmentActivity extends AppCompatActivity implements RecyclerMoveItemTouchHelper.RecyclerItemTouchHelperListener {

    private RecyclerView recyclerView;
    private List<Appointment> appointmentList;
    private AppointmentMoveAdapter mAdapter;
    CoordinatorLayout coordinatorLayout;
    AppointmentDbHandler appointmentDbHandler;
    Context context = MoveAppointmentActivity.this;
    String date;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.move_list));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();
        date = intent.getStringExtra("createdDate");

        recyclerView = findViewById(R.id.recycler_view);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        appointmentDbHandler = new AppointmentDbHandler(context);
        appointmentDbHandler.open();
        appointmentList = appointmentDbHandler.getAllAppointmentsForSelectedDate(date);
        appointmentDbHandler.close();
        mAdapter = new AppointmentMoveAdapter(appointmentList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerMoveItemTouchHelper(0, ItemTouchHelper.LEFT, this);
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
                // remove it from adapter
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
        if (viewHolder instanceof AppointmentMoveAdapter.MoveViewHolder) {
            appointmentList.get(viewHolder.getAdapterPosition()).getAppointmentTitle();

            final Appointment movedAppointment = appointmentList.get(viewHolder.getAdapterPosition());
            String appointmentId = movedAppointment.getAppointmentId();
            String appointmentTitle = movedAppointment.getAppointmentTitle();
            String appointmentCreatedDate = movedAppointment.getAppointmentCreatedDate();
            String appointmentCreatedTime = movedAppointment.getAppointmentCreatedTime();
            String appointmentDetails = movedAppointment.getAppointmentDetails();

            Intent intent = new Intent(MoveAppointmentActivity.this, MoveItemActivity.class);
            intent.putExtra("moveAppointmentId",appointmentId);
            intent.putExtra("moveAppointmentTitle",appointmentTitle);
            intent.putExtra("moveAppointmentCreatedDate",appointmentCreatedDate);
            intent.putExtra("moveAppointmentCreatedTime",appointmentCreatedTime);
            intent.putExtra("moveAppointmentDetails",appointmentDetails);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(MoveAppointmentActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        Intent intent = new Intent(MoveAppointmentActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}
