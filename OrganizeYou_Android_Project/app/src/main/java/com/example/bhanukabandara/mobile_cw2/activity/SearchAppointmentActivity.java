package com.example.bhanukabandara.mobile_cw2.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.example.bhanukabandara.mobile_cw2.R;
import com.example.bhanukabandara.mobile_cw2.adapter.AppointmentSearchAdapter;
import com.example.bhanukabandara.mobile_cw2.dbhandler.AppointmentDbHandler;
import com.example.bhanukabandara.mobile_cw2.model.Appointment;
import java.util.ArrayList;
import java.util.List;

public class SearchAppointmentActivity extends AppCompatActivity implements AppointmentSearchAdapter.AppointmentAdapterListener{

    private RecyclerView recyclerView;
    private List<Appointment> appointmentList;
    private AppointmentSearchAdapter mAdapter;
    private SearchView searchView;
    AppointmentDbHandler appointmentDbHandler;
    Context context = SearchAppointmentActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.search_list);

        recyclerView = findViewById(R.id.recycler_view);
        appointmentDbHandler = new AppointmentDbHandler(context);
        appointmentDbHandler.open();
        appointmentList = new ArrayList<>();
        appointmentList = appointmentDbHandler.getAllAppointments();
        appointmentDbHandler.close();
        mAdapter = new AppointmentSearchAdapter(context, appointmentList, this);

        // white background notification bar
        whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        prepareAppointmentData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(SearchAppointmentActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            Intent intent = new Intent(SearchAppointmentActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onAppointmentSelected(Appointment appointment) {
        Toast.makeText(getApplicationContext(), "Selected: " + appointment.getAppointmentTitle() + ", " + appointment.getAppointmentDetails(), Toast.LENGTH_LONG).show();
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
}
