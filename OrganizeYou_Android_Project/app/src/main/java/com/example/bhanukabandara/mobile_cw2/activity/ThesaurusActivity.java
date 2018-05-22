package com.example.bhanukabandara.mobile_cw2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.example.bhanukabandara.mobile_cw2.R;
import com.example.bhanukabandara.mobile_cw2.adapter.ThesaurusDisplayAdapter;
import java.util.ArrayList;

public class ThesaurusActivity extends AppCompatActivity {

    private ArrayList<String> synonymsArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ThesaurusDisplayAdapter mAdapter;
    String appointmentTitle, appointmentDetails, appointmentTime, appointmentCreatedDate;
    String editItemAppointmentTitle, editItemAppointmentCreatedTime, editItemAppointmentDetails, editItemAppointmentCreatedDate;
    boolean isFromCreateAppointment = false;
    boolean isFromEditItemAppointment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thesaurus_list_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.synonyms_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recycler_view);

        Bundle bundle = getIntent().getExtras();
        synonymsArrayList = bundle.getStringArrayList("synonymsArray");
        appointmentTitle = bundle.getString("appointmentTitle");
        appointmentDetails = bundle.getString("appointmentDetails");
        appointmentTime = bundle.getString("appointmentTime");
        appointmentCreatedDate = bundle.getString("createdDate");
        isFromCreateAppointment = bundle.getBoolean("isFromCreateAppointment");
        isFromEditItemAppointment = bundle.getBoolean("isFromEditItemAppointment");

        editItemAppointmentTitle = bundle.getString("editItemAppointmentTitle");
        editItemAppointmentDetails = bundle.getString("editItemAppointmentDetails");
        editItemAppointmentCreatedTime = bundle.getString("editItemAppointmentTime");
        editItemAppointmentCreatedDate = bundle.getString("editAppointmentCreatedDate");

        mAdapter = new ThesaurusDisplayAdapter(synonymsArrayList);

        recyclerView.setHasFixedSize(true);



        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

        // row click listener
        recyclerView.addOnItemTouchListener(new RecyclerThesaurusItemListner(getApplicationContext(), recyclerView, new RecyclerThesaurusItemListner.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String thesaurusWordSelected = synonymsArrayList.get(position);
                String arr[] = thesaurusWordSelected.split("\\(");
                String removeDelimeterSynonym = arr[0];
                Toast.makeText(getApplicationContext(), removeDelimeterSynonym + " is selected!", Toast.LENGTH_SHORT).show();

                if (isFromCreateAppointment){
                    Intent intent = new Intent(ThesaurusActivity.this, CreateAppointmentActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("synonymWordSelected", removeDelimeterSynonym);
                    intent.putExtra("appointmentTitle",appointmentTitle);
                    intent.putExtra("appointmentDetails",appointmentDetails);
                    intent.putExtra("appointmentTime",appointmentTime);
                    intent.putExtra("createdDate",appointmentCreatedDate);
                    startActivity(intent);

                }else if ( isFromEditItemAppointment) {
                    Intent intent = new Intent(ThesaurusActivity.this, EditItemActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("synonymWordSelected", removeDelimeterSynonym);
                    intent.putExtra("appointmentTitle",editItemAppointmentTitle);
                    intent.putExtra("appointmentDetails",editItemAppointmentDetails);
                    intent.putExtra("appointmentTime",editItemAppointmentCreatedTime);
                    intent.putExtra("createdDate",editItemAppointmentCreatedDate);
                    startActivity(intent);
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(ThesaurusActivity.this, CreateAppointmentActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("appointmentTitle",appointmentTitle);
            intent.putExtra("appointmentDetails",appointmentDetails);
            intent.putExtra("appointmentTime",appointmentTime);
            intent.putExtra("appointmentCreatedDate",appointmentCreatedDate);
            startActivity(intent);
            return true;
        }
        return false;
    }

}
