package com.example.bhanukabandara.mobile_cw2.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;
import com.example.bhanukabandara.mobile_cw2.R;
import com.example.bhanukabandara.mobile_cw2.adapter.AppointmentDeleteAdapter;
import com.example.bhanukabandara.mobile_cw2.dbhandler.AppointmentDbHandler;
import com.example.bhanukabandara.mobile_cw2.model.Appointment;
import java.util.List;

public class DeleteAppointmentActivity extends AppCompatActivity implements RecyclerDeleteItemTouchHelper.RecyclerItemTouchHelperListener {

    private RecyclerView recyclerView;
    private List<Appointment> appointmentList;
    private AppointmentDeleteAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;
    AppointmentDbHandler appointmentDbHandler;
    Context context = DeleteAppointmentActivity.this;
    Intent intent;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.delete_list));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();
        date = intent.getStringExtra("createdDate");

        recyclerView = findViewById(R.id.recycler_view);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        appointmentDbHandler = new AppointmentDbHandler(context);
        appointmentDbHandler.open();
        appointmentList = appointmentDbHandler.getAllAppointmentsForSelectedDate(date);
        appointmentDbHandler.close();
        mAdapter = new AppointmentDeleteAdapter(appointmentList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerDeleteItemTouchHelper(0, ItemTouchHelper.LEFT, this);
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
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        if (viewHolder instanceof AppointmentDeleteAdapter.DeleteViewHolder) {
            // get the removed item appointmentTitle to display it in snack bar
            String name = appointmentList.get(viewHolder.getAdapterPosition()).getAppointmentTitle();

            // backup of removed item for undo purpose
            final Appointment deletedItem = appointmentList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            // set title
            alertDialogBuilder.setTitle("Confirm To Delete");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Are you sure you want to delete "+deletedItem.getAppointmentTitle()+" appointment? ")
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // remove the item from recycler view
                            mAdapter.removeItem(viewHolder.getAdapterPosition(),context);

                        }
                    });

            alertDialogBuilder
                    .setCancelable(false)
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close the dialog
                            dialog.cancel();
                            mAdapter.notifyDataSetChanged();

                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds appointmentList to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        Intent intent = new Intent(DeleteAppointmentActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(DeleteAppointmentActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }

        if (item.getItemId() == R.id.action_delete_all) {
            Toast.makeText(DeleteAppointmentActivity.this,"DELETE ALL CLICKED",Toast.LENGTH_LONG).show();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            // set title
            alertDialogBuilder.setTitle("Confirm To Delete");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Are you sure you want to delete all appointments? ")
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close the dialog
                            appointmentDbHandler.open();
                            boolean isDeletedAll = appointmentDbHandler.deleteAppointmentForSelectedDate(date);
                            appointmentDbHandler.close();
                            mAdapter.notifyDataSetChanged();
                            if (isDeletedAll){
                                Toast.makeText(DeleteAppointmentActivity.this,"All records deleted successfully",Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(getIntent());
                            }else if (!isDeletedAll){
                                Toast.makeText(DeleteAppointmentActivity.this,"Something went wrong when deleting",Toast.LENGTH_LONG).show();
                            }

                        }
                    });

            alertDialogBuilder
                    .setCancelable(false)
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close the dialog
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
            return true;
        }
        return false;
    }
}
